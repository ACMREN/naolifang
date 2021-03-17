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
    private String region;
    private String maintainPerson;
    private String status;
    private String doorStatus;
    private String createTime;
    private Long liveTime;

    public DeviceInfoVo() {
    }

    public DeviceInfoVo(DeviceInfo deviceInfo) {
        this.id = deviceInfo.getId();
        this.name = deviceInfo.getName();
        this.type = DeviceTypeEnum.getDataByCode(deviceInfo.getType()).getName();
        this.indexCode = deviceInfo.getIndexCode();
        this.ip = deviceInfo.getIp();
        this.manufacturer = deviceInfo.getManufacturer();
        this.position = deviceInfo.getPosition();
        this.region = RegionEnum.getDataByCode(deviceInfo.getRegion()).getName();
        this.maintainPerson = deviceInfo.getMaintainPerson();
        this.status = StatusEnum.getDataByCode(deviceInfo.getStatus()).getName();
        this.doorStatus = DoorStatusEnum.getDataByCode(deviceInfo.getDoorStatus()).getName();
        if (null != deviceInfo.getCreateTime()) {
            this.createTime = DateTimeUtil.localDateTimeToString(deviceInfo.getCreateTime());
        }
        this.liveTime = deviceInfo.getLiveTime();
    }
}
