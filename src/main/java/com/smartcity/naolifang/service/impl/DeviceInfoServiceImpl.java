package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.AlarmEventInfo;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.HandleStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.mapper.DeviceInfoMapper;
import com.smartcity.naolifang.service.AlarmEventInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备信息表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-15
 */
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Autowired
    private Config config;

    @Autowired
    private AlarmEventInfoService alarmEventInfoService;

    @Override
    public boolean verifyDevice(List<String> indexCodes, String type) {
        String dataStr = this.getDeviceByApi(indexCodes, type);
        JSONObject dataJson = JSONObject.parseObject(dataStr);
        if (null == dataJson || dataJson.getJSONObject("data").getString("list") == null) {
            return false;
        }

        return true;
    }

    @Override
    public String getDeviceByApi(List<String> indexCodes, String type) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("resourceType", type);
        paramMap.put("resourceIndexCodes", indexCodes);

        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionDeviceSearchUrl(), paramMap);
        return resultStr;
    }

    @Override
    public void judgeDeviceStatus(List<String> indexCodes, Integer type) {
        String requestUrl = "";
        if (type.equals(DeviceTypeEnum.ENCODE_DEVICE.getCode())) {
            requestUrl = config.getHikivisionEncodeDeviceStatusUrl();
        }
        if (type.equals(DeviceTypeEnum.DOOR.getCode())) {
            requestUrl = config.getHikivisionDoorStatusUrl();
        }
        if (type.equals(DeviceTypeEnum.CAMERA.getCode())) {
            requestUrl = config.getHikivisionCameraStatusUrl();
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("indexCodes", indexCodes);

        String resultStr = HttpUtil.postToHikvisionPlatform(requestUrl, paramMap);
        if (StringUtils.isBlank(resultStr)) {
            return;
        }
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        JSONArray deviceJsons = resultJson.getJSONObject("data").getJSONArray("list");
        List<String> includeIndexCodes = new ArrayList<>();

        // 1. 更新设备在线离线状态
        if (!CollectionUtils.isEmpty(deviceJsons)) {
            for (Object item : deviceJsons) {
                JSONObject deviceJson = (JSONObject) item;
                Integer online = deviceJson.getInteger("online");
                String indexCode = deviceJson.getString("indexCode");
                DeviceInfo deviceInfo = this.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", indexCode));
                if (null != deviceInfo) {
                    if (online.intValue() == 1) {
                        deviceInfo.setStatus(StatusEnum.ONLINE.getCode());
                        this.saveOrUpdate(deviceInfo);
                    }
                    if (online.intValue() == 0) {
                        deviceInfo.setStatus(StatusEnum.OFFLINE.getCode());
                        this.saveOrUpdate(deviceInfo);

                        // 查看是否已经存在一条该设备还没处理的离线告警
                        // 如果不存在则创建新的设备告警
                        AlarmEventInfo alarmEventInfo = alarmEventInfoService.getOne(new QueryWrapper<AlarmEventInfo>()
                                .eq("device_id", deviceInfo.getId())
                                .eq("event_id", "0")
                                .ne("status", HandleStatusEnum.HANDLED.getCode()));
                        if (null == alarmEventInfo) {
                            alarmEventInfo = new AlarmEventInfo();
                            alarmEventInfo.setAlarmType(0);
                            alarmEventInfo.setContent("离线");
                            alarmEventInfo.setEventId("0");
                            alarmEventInfo.setDeviceId(deviceInfo.getId());
                            alarmEventInfo.setStatus(0);
                            alarmEventInfo.setAlarmTime(LocalDateTime.now());
                            alarmEventInfoService.saveOrUpdate(alarmEventInfo);
                        }
                    }
                    includeIndexCodes.add(indexCode);
                }
            }
        }

        // 2. 不在返回列表的设备则是未激活状态
        indexCodes.removeAll(includeIndexCodes);
        for (String item : indexCodes) {
            DeviceInfo deviceInfo = this.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", item));
            deviceInfo.setStatus(StatusEnum.INACTIVE.getCode());
            this.saveOrUpdate(deviceInfo);
        }
    }
}
