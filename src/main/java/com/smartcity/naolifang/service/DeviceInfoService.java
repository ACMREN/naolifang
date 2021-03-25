package com.smartcity.naolifang.service;

import com.smartcity.naolifang.entity.DeviceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-15
 */
public interface DeviceInfoService extends IService<DeviceInfo> {

    boolean verifyDevice(List<String> indexCodes, String type);

    String getDeviceByApi(List<String> indexCodes, String type);

    boolean judgeDeviceStatus(List<String> indexCodes, String type);

}
