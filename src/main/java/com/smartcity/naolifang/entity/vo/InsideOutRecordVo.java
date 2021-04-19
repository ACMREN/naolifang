package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.InsideOutRecord;
import lombok.Data;

@Data
public class InsideOutRecordVo {
    private Integer id;
    private Integer personId;
    private String name;
    private String idCard;
    private String imageUri;
    private Integer personType;
    private Integer deviceId;
    private String deviceName;
    private String positionInfo;
    private Integer type;
    private String time;

    public InsideOutRecordVo() {
    }

    public InsideOutRecordVo(InsideOutRecord insideOutRecord) {
        this.id = insideOutRecord.getId();
        this.name = insideOutRecord.getName();
        this.idCard = insideOutRecord.getIdCard();
        this.personId = insideOutRecord.getPersonId();
        this.imageUri = insideOutRecord.getImageUri();
        this.personType = insideOutRecord.getPersonType();
        this.deviceId = insideOutRecord.getDeviceId();
        this.deviceName = insideOutRecord.getDeviceName();
        this.positionInfo = insideOutRecord.getPositionInfo();
        this.type = insideOutRecord.getType();
        this.time = DateTimeUtil.localDateTimeToString(insideOutRecord.getTime());
    }
}
