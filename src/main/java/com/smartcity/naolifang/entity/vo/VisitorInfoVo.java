package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.VisitorInfo;
import lombok.Data;

@Data
public class VisitorInfoVo {
    private Integer id;
    private String name;
    private String idCard;
    private String phone;
    private String visitPerson;
    private String reason;
    private String visitStartTime;
    private String visitEndTime;
    private String leaveTime;
    private String originalPlace;
    private Integer status;
    private String imageUri;

    public VisitorInfoVo() {
    }

    public VisitorInfoVo(VisitorInfo visitorInfo) {
        this.id = visitorInfo.getId();
        this.name = visitorInfo.getName();
        this.idCard = visitorInfo.getIdCard();
        this.phone = visitorInfo.getPhone();
        this.visitPerson = visitorInfo.getVisitPerson();
        this.reason = visitorInfo.getReason();
        this.visitStartTime = DateTimeUtil.localDateTimeToString(visitorInfo.getVisitStartTime());
        this.visitEndTime = DateTimeUtil.localDateTimeToString(visitorInfo.getVisitEndTime());
        if (null != visitorInfo.getLeaveTime()) {
            this.leaveTime = DateTimeUtil.localDateTimeToString(visitorInfo.getLeaveTime());
        }
        this.originalPlace = visitorInfo.getOriginalPlace();
        this.status = visitorInfo.getStatus();
        this.imageUri = visitorInfo.getImageUri();
    }
}
