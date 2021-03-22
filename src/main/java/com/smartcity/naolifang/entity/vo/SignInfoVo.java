package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.SignInfo;
import lombok.Data;

@Data
public class SignInfoVo {
    private Integer id;
    private Integer visitorId;
    private String temperature;
    private String healthCode;
    private String originalPlace;
    private String checkTime;
    private String checkHospital;

    public SignInfoVo() {
    }

    public SignInfoVo(SignInfo signInfo) {
        this.id = signInfo.getId();
        this.visitorId = signInfo.getVisitorId();
        this.temperature = signInfo.getTemperature();
        this.healthCode = signInfo.getHealthCode();
        this.originalPlace = signInfo.getOriginalPlace();
        this.checkTime = DateTimeUtil.localDateToString(signInfo.getCheckTime());
        this.checkHospital = signInfo.getCheckHospital();
    }
}
