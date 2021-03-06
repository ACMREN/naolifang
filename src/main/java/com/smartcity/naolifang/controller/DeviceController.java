package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.common.util.SystemUtil;
import com.smartcity.naolifang.entity.CameraPollingInfo;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.DoorStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.searchCondition.DeviceCondition;
import com.smartcity.naolifang.entity.vo.DeviceInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.CameraPollingInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/iotPlatform")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private CameraPollingInfoService cameraPollingInfoService;

    @Autowired
    private Config config;

    /**
     * 订阅海康事件消息
     * @param deviceCondition
     * @return
     */
    @RequestMapping("/hikivision/subscribeEvent/subscribe")
    public Result subscribeEvent(@RequestBody DeviceCondition deviceCondition) {
        Map<String, Object> paramMap = new HashMap<>();
        List<Integer> eventTypes = deviceCondition.getEventTypes();
        String callBackApi = deviceCondition.getCallBackApi();
        String localAddress = SystemUtil.getLocalAddress();
        String callBackUrl = localAddress.concat(":2020").concat(callBackApi);

        paramMap.put("eventTypes", eventTypes);
        paramMap.put("eventDest", callBackUrl);

        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionSubscribeEventUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }
        return Result.ok();
    }

    /**
     * 查看订阅的事件
     * @return
     */
    @RequestMapping("/hikivision/subscribeEvent/check")
    public Result checkSubscribe() {
        Map<String, Object> paramMap = new HashMap<>();

        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionCheckSubscribeEventUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);

        return Result.ok(resultJson);
    }

    /**
     * 取消订阅事件
     * @param deviceCondition
     * @return
     */
    @RequestMapping("/hikivision/subscribeEvent/unsubscribe")
    public Result unsubscribeEvent(@RequestBody DeviceCondition deviceCondition) {
        Map<String, Object> paramMap = new HashMap<>();
        List<Integer> eventTypes = deviceCondition.getEventTypes();

        paramMap.put("eventTypes", eventTypes);

        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionUnsunscribeEventUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }
        return Result.ok();
    }

    @RequestMapping("/hikivision/config/")
    public Result getHikivisionConfig() {
        String appKey = ArtemisConfig.appKey = "26930432";
        String appSecret = ArtemisConfig.appSecret = "ZbBuQUbPytNgIktNtBoF";
        String ip = "192.168.1.24";

        JSONObject resultJson = new JSONObject();
        resultJson.put("appkey", appKey);
        resultJson.put("secret", appSecret);
        resultJson.put("ip", ip);
        return Result.ok(resultJson);
    }

    @RequestMapping("/info/save")
    public Result saveDevice(@RequestBody DeviceInfoVo deviceInfoVo) {
        DeviceInfo deviceInfo = new DeviceInfo(deviceInfoVo);

        // 如果是新建的设备，那么要判断设备是否匹配
        if (null == deviceInfoVo.getStatus()) {
            String indexCode = deviceInfo.getIndexCode();
            String typeNameEn = DeviceTypeEnum.getDataByCode(deviceInfo.getType()).getNameEn();
            List<String> indexCodes = new ArrayList<>();
            indexCodes.add(indexCode);
            boolean match = deviceInfoService.verifyDevice(indexCodes, typeNameEn);
            if (match) {
                deviceInfo.setConnectTime(LocalDateTime.now());
                deviceInfo.setStatus(StatusEnum.ONLINE.getCode());
            } else {
                deviceInfo.setStatus(StatusEnum.INACTIVE.getCode());
            }
        }

        deviceInfoService.saveOrUpdate(deviceInfo);

        return Result.ok();
    }

    @RequestMapping("/info/list")
    public Result listDevice(@RequestBody DeviceCondition deviceCondition) {
        logger.info("test");
        Integer pageNo = deviceCondition.getPageNo();
        Integer pageSize = deviceCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取设备列表信息失败，信息：传入的页码或数据条数为空");
        }
        Integer typeInt = 0;
        Integer offset = (pageNo - 1) * pageSize;
        String indexCode = deviceCondition.getIndexCode();
        Integer region = deviceCondition.getRegion();
        Integer status = deviceCondition.getStatus();
        String type = deviceCondition.getType();
        String connectStartTime = deviceCondition.getConnectStartTime();
        String connectEndTime = deviceCondition.getConnectEndTime();
        if (StringUtils.isNotBlank(type)) {
            typeInt = DeviceTypeEnum.getDataByNameEn(deviceCondition.getType()).getCode();
        }

        List<DeviceInfo> dataList = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(type), "type", typeInt)
                .eq(null != status, "status", status)
                .eq(null != region, "region", region)
                .like(StringUtils.isNotBlank(indexCode), "index_code", indexCode)
                .gt(StringUtils.isNotBlank(connectStartTime), "connect_time", connectStartTime)
                .lt(StringUtils.isNotBlank(connectEndTime), "connect_time", connectEndTime)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(type), "type", typeInt)
                .eq(null != status, "status", status)
                .eq(null != region, "region", region)
                .like(StringUtils.isNotBlank(indexCode), "index_code", indexCode)
                .gt(StringUtils.isNotBlank(connectStartTime), "connect_time", connectStartTime)
                .lt(StringUtils.isNotBlank(connectEndTime), "connect_time", connectEndTime));

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

        // 把用户大屏显示中的摄像头轮询列表对应信息删除
        cameraPollingInfoService.remove(new QueryWrapper<CameraPollingInfo>().eq("camera_id", ids));

        return Result.ok();
    }

    @RequestMapping("/info/switch")
    public Result forbidDevice(@RequestBody DeviceCondition deviceCondition) {
        List<Integer> ids = deviceCondition.getIds();
        Integer changeStatus = deviceCondition.getChangeStatus();
        List<DeviceInfo> deviceInfos = deviceInfoService.listByIds(ids);
        for (DeviceInfo item : deviceInfos) {
            item.setStatus(changeStatus);
            // 如果是禁用设备，则要计算生命周期
            if (changeStatus.intValue() == 3) {
                LocalDateTime createTime = item.getConnectTime();
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(createTime, now);
                long liveTime = duration.toMillis();
                item.setLiveTime(liveTime);
            }
            // 如果是启用设备，则要重新设备上线时间
            if (changeStatus.intValue() == 1) {
                item.setConnectTime(LocalDateTime.now());
            }
        }

        deviceInfoService.saveOrUpdateBatch(deviceInfos);

        return Result.ok();
    }

    @RequestMapping("/control/door")
    public Result doorControl(@RequestBody DeviceCondition deviceCondition) {
        List<String> indexCodes = deviceCondition.getIndexCodes();
        Integer controlType = deviceCondition.getControlType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("doorIndexCodes", indexCodes);
        paramMap.put("controlType", controlType);

        String resultStr = HttpUtil.doPost(config.getHikivisionPlatformUrl() + config.getHikivisionDoorControlUrl(), paramMap);
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
                if (controlType.intValue() == 0) {
                    deviceInfo.setDoorStatus(DoorStatusEnum.OPEN.getCode());
                }
                if (controlType.intValue() == 1 || controlType.intValue() == 2) {
                    deviceInfo.setDoorStatus(DoorStatusEnum.NORMAL.getCode());
                }
                if (controlType.intValue() == 3) {
                    deviceInfo.setDoorStatus(DoorStatusEnum.CLOSE.getCode());
                }
                deviceInfoService.saveOrUpdate(deviceInfo);
            }
        }

        return Result.ok(failList);
    }

    @RequestMapping("/simulate")
    public Result simulateDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setName("模拟门禁名称" + i);
            deviceInfo.setType(DeviceTypeEnum.DOOR.getCode());
            deviceInfo.setIndexCode(UUID.randomUUID().toString());
            deviceInfo.setIp("192.168.1.1");
            deviceInfo.setPositionInfo("模拟设备位置" + i);
            deviceInfo.setMaintainPerson("张三");
            deviceInfo.setStatus(StatusEnum.ONLINE.getCode());
            deviceInfo.setConnectTime(LocalDateTime.now());
            deviceInfos.add(deviceInfo);
        }
        deviceInfoService.saveOrUpdateBatch(deviceInfos);

        return Result.ok();
    }
}
