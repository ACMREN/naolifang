package com.smartcity.naolifang.config.scheduleJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckDeviceStatusJob {

    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * 定时更新设备状态
     */
    @Scheduled(cron = "* 0/5 * * * ?")
    public void checkDeviceStatusJob() {
        List<DeviceInfo> deviceInfos = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("status", StatusEnum.OFFLINE)
                .or().eq("status", StatusEnum.INACTIVE));

        List<String> cameraIndexCodes = deviceInfos.stream()
                .filter(item -> item.getType().equals(DeviceTypeEnum.CAMERA.getCode()))
                .map(DeviceInfo::getIndexCode)
                .collect(Collectors.toList());
        List<String> doorIndexCodes = deviceInfos.stream()
                .filter(item -> item.getType().equals(DeviceTypeEnum.DOOR.getCode()))
                .map(DeviceInfo::getIndexCode)
                .collect(Collectors.toList());
        List<String> encodeIndexCodes = deviceInfos.stream()
                .filter(item -> item.getType().equals(DeviceTypeEnum.ENCODE_DEVICE.getCode()))
                .map(DeviceInfo::getIndexCode)
                .collect(Collectors.toList());

        deviceInfoService.judgeDeviceStatus(cameraIndexCodes, DeviceTypeEnum.CAMERA.getCode());
        deviceInfoService.judgeDeviceStatus(doorIndexCodes, DeviceTypeEnum.DOOR.getCode());
        deviceInfoService.judgeDeviceStatus(encodeIndexCodes, DeviceTypeEnum.ENCODE_DEVICE.getCode());
    }
}