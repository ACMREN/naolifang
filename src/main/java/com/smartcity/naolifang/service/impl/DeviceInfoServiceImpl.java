package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.CameraAlarmTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.mapper.DeviceInfoMapper;
import com.smartcity.naolifang.service.AlarmEventInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        boolean result = false;
        String dataStr = this.getDeviceByApi(indexCodes, type);
        JSONObject dataJson = JSONObject.parseObject(dataStr);
        if (dataJson.getJSONObject("data").getString("list") == null) {
            return false;
        }

        return result;
    }

    /**
     * 验证输入参数是否符合获取到的参数
     * @param deviceJson
     * @param deviceInfo
     * @return
     */
    private boolean verifyDeviceParam(JSONObject deviceJson, DeviceInfo deviceInfo) {
        if (!deviceInfo.getName().equals(deviceJson.getString("name"))) {
            return false;
        }
        if (!deviceInfo.getIp().equals(deviceJson.getString("ip"))) {
            return false;
        }
        if (!deviceInfo.getManufacturer().equals(deviceJson.getString("manufacturer"))) {
            return false;
        }
        return true;
    }

    @Override
    public String getDeviceByApi(List<String> indexCodes, String type) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("resourceType", type);
        paramMap.put("resourceIndexCodes", indexCodes);

        String resultStr = HttpUtil.doPost(config.getHikivisionDeviceSearchUrl(), paramMap);
        return resultStr;
    }

    @Override
    public boolean judgeDeviceStatus(List<String> indexCodes, String type) {
        String requestUrl = "";
        if (type.equals(DeviceTypeEnum.ENCODE_DEVICE.getName())) {
            requestUrl = config.getHikivisionEncodeDeviceStatusUrl();
        }
        if (type.equals(DeviceTypeEnum.DOOR.getName())) {
            requestUrl = config.getHikivisionDoorStatusUrl();
        }
        if (type.equals(DeviceTypeEnum.CAMERA.getName())) {
            requestUrl = config.getHikivisionCameraStatusUrl();
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("indexCodes", indexCodes);
        paramMap.put("status", 1);

        String resultStr = HttpUtil.doPost(requestUrl, paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        JSONObject deviceJson = resultJson.getJSONObject("data").getJSONArray("list").getJSONObject(0);

        Integer online = deviceJson.getInteger("online");
        if (online == 1) {
            return true;
        }
        return false;
    }
}
