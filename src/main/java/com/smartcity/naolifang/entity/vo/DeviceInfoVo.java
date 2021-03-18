package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.DoorStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.RegionEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import lombok.Data;

@Data
public class DeviceInfoVo {
    private Integer id;
    private String name;
    private String type;
    private String indexCode;
    private String ip;
    private String manufacturer;
    private String position;
    private Integer region;
    private String maintainPerson;
    private Integer status;
    private Integer doorStatus;
    private String createTime;
    private Long liveTime;

    public DeviceInfoVo() {
    }

    public DeviceInfoVo(DeviceInfo deviceInfo) {
        this.id = deviceInfo.getId();
        this.name = deviceInfo.getName();
        this.type = DeviceTypeEnum.getDataByCode(deviceInfo.getType()).getNameEn();
        this.indexCode = deviceInfo.getIndexCode();
        this.ip = deviceInfo.getIp();
        this.manufacturer = deviceInfo.getManufacturer();
        this.position = deviceInfo.getPosition();
        this.region = deviceInfo.getRegion();
        this.maintainPerson = deviceInfo.getMaintainPerson();
        this.status = deviceInfo.getStatus();
        this.doorStatus = deviceInfo.getDoorStatus();
        if (null != deviceInfo.getCreateTime()) {
            this.createTime = DateTimeUtil.localDateTimeToString(deviceInfo.getCreateTime());
        }
        this.liveTime = deviceInfo.getLiveTime();
    }
}
