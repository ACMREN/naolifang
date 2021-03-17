package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.searchCondition.DeviceCondition;
import com.smartcity.naolifang.entity.vo.DeviceInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iotPlatform")
public class DeviceController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private Config config;

    @RequestMapping("/info/save")
    public Result saveDevice(@RequestBody DeviceInfoVo deviceInfoVo) {
        DeviceInfo deviceInfo = new DeviceInfo(deviceInfoVo);
        String indexCode = deviceInfo.getIndexCode();
        String typeNameEn = DeviceTypeEnum.getDataByCode(deviceInfo.getType()).getNameEn();
        List<String> indexCodes = new ArrayList<>();
        indexCodes.add(indexCode);
        boolean match = deviceInfoService.verifyDevice(indexCodes, typeNameEn);
        if (match) {
            deviceInfo.setCreateTime(LocalDateTime.now());
            deviceInfo.setStatus(StatusEnum.ONLINE.getCode());
        } else {
            deviceInfo.setStatus(StatusEnum.INACTIVE.getCode());
        }

        deviceInfoService.saveOrUpdate(deviceInfo);

        return Result.ok();
    }

    @RequestMapping("/info/list")
    public Result listDevice(@RequestBody DeviceCondition deviceCondition) {
        Integer pageNo = deviceCondition.getPageNo();
        Integer pageSize = deviceCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取设备列表信息失败，信息：传入的页码或数据条数为空");
        }
        Integer statusInt = 0;
        Integer typeInt = 0;
        Integer offset = (pageNo - 1) * pageSize;
        String indexCode = deviceCondition.getIndex();
        String position = deviceCondition.getPosition();
        String status = deviceCondition.getStatus();
        String type = deviceCondition.getType();
        String createStartTime = deviceCondition.getCreateStartTime();
        String createEndTime = deviceCondition.getCreateEndTime();
        if (StringUtils.isNotBlank(status)) {
            statusInt = StatusEnum.getDataByName(deviceCondition.getStatus()).getCode();
        }
        if (StringUtils.isNotBlank(type)) {
            typeInt = DeviceTypeEnum.getDataByName(deviceCondition.getType()).getCode();
        }

        List<DeviceInfo> dataList = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(type), "type", typeInt)
                .eq(StringUtils.isNotBlank(status), "status", statusInt)
                .like(StringUtils.isNotBlank(indexCode), "index_code", indexCode)
                .like(StringUtils.isNotBlank(position), "position", position)
                .gt(StringUtils.isNotBlank(createStartTime), "create_time", createStartTime)
                .lt(StringUtils.isNotBlank(createEndTime), "create_time", createEndTime)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(type), "type", typeInt)
                .eq(StringUtils.isNotBlank(status), "status", statusInt)
                .like(StringUtils.isNotBlank(indexCode), "index_code", indexCode)
                .like(StringUtils.isNotBlank(position), "position", position)
                .gt(StringUtils.isNotBlank(createStartTime), "create_time", createStartTime)
                .lt(StringUtils.isNotBlank(createEndTime), "create_time", createEndTime));

        List<DeviceInfoVo> resultList = new ArrayList<>();
        for (DeviceInfo item : dataList) {
            DeviceInfoVo data = new DeviceInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/info/remove")
    public Result deleteDevice(@RequestBody DeviceCondition deviceCondition) {
        List<Integer> ids = deviceCondition.getIds();

        List<DeviceInfo> dataList = deviceInfoService.listByIds(ids);
        for (DeviceInfo item : dataList) {
            item.setIsDelete(1);
        }
        deviceInfoService.saveOrUpdateBatch(dataList);

        return Result.ok();
    }

    @RequestMapping("/info/switch")
    public Result forbidDevice(@RequestBody DeviceCondition deviceCondition) {
        List<Integer> ids = deviceCondition.getIds();
        String changeStatus = deviceCondition.getChangeStatus();
        List<DeviceInfo> deviceInfos = deviceInfoService.listByIds(ids);
        for (DeviceInfo item : deviceInfos) {
            item.setStatus(StatusEnum.getDataByName(changeStatus).getCode());
            // 如果是禁用设备，则要计算生命周期
            if (changeStatus.equals(StatusEnum.FORBID.getName())) {
                LocalDateTime createTime = item.getCreateTime();
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(createTime, now);
                long liveTime = duration.toMillis();
                item.setLiveTime(liveTime);
            }
            // 如果是启用设备，则要重新设备上线时间
            if (changeStatus.equals(StatusEnum.ONLINE.getName())) {
                item.setCreateTime(LocalDateTime.now());
            }
        }

        deviceInfoService.saveOrUpdateBatch(deviceInfos);

        return Result.ok();
    }

    @RequestMapping("/control/gate")
    public Result doorControl(@RequestBody DeviceCondition deviceCondition) {
        List<String> indexCodes = deviceCondition.getIndexCodes();
        String controlType = deviceCondition.getControlType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("doorIndexCodes", indexCodes);
        paramMap.put("controlType", controlType);

        String resultStr = HttpUtil.doPost(config.getHikivisionDoorControlUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        List<JSONObject> doorResultList = JSONObject.parseArray(resultJson.getString("data"), JSONObject.class);

        // 记录失败的门禁
        List<String> failList = new ArrayList<>();
        for (JSONObject data : doorResultList) {
            Integer controlResultCode = data.getInteger("controlResultCode");
            if (controlResultCode.intValue() != 0) {
                StringBuilder sb = new StringBuilder();
                String doorIndexCode = data.getString("doorIndexCode");
                sb.append("门禁编号").append("(").append(doorIndexCode).append(")").append("操作失败").append("，错误码").append(controlResultCode);
                failList.add(sb.toString());
            }
        }

        // 设置成功门禁状态
        for (JSONObject data : doorResultList) {
            Integer controlResultCode = data.getInteger("controlResultCode");
            if (controlResultCode.intValue() == 0) {
                String doorIndexCode = data.getString("doorIndexCode");
                DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>()
                        .eq("index_code", doorIndexCode));
                deviceInfo.setDoorStatus(Integer.valueOf(controlType));
                deviceInfoService.saveOrUpdate(deviceInfo);
            }
        }

        return Result.ok(failList);
    }
}
