package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.InsiderInfo;
import lombok.Data;

@Data
public class InsiderInfoVo {
    private Integer id;
    private String name;
    private String idCard;
    private String phone;
    private String groupName;
    private String superior;
    private String imageUri;
    private Integer isDelete;
    private String createTime;
    private String updateTime;

    public InsiderInfoVo() {
    }

    public InsiderInfoVo(InsiderInfo insiderInfo) {
        this.id = insiderInfo.getId();
        this.name = insiderInfo.getName();
        this.idCard = insiderInfo.getIdCard();
        this.phone = insiderInfo.getPhone();
        this.groupName = insiderInfo.getGroupName();
        this.superior = insiderInfo.getSuperior();
        this.imageUri = insiderInfo.getImageUri();
        this.isDelete = insiderInfo.getIsDelete();
        this.createTime = DateTimeUtil.localDateTimeToString(insiderInfo.getCreateTime());
        this.updateTime = DateTimeUtil.localDateTimeToString(insiderInfo.getUpdateTime());
    }
}
