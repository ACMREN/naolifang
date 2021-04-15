package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import lombok.Data;

@Data
public class DependantInfoVo {
    private Integer id;
    private String name;
    private String gender;
    private String idCard;
    private String relation;
    private String phone;
    private String relationship;
    private String institution;
    private String address;
    private String imageUri;
    private Integer isDelete;
    private String createTime;
    private String updateTime;

    public DependantInfoVo() {
    }

    public DependantInfoVo(DependantInfo dependantInfo) {
        this.id = dependantInfo.getId();
        this.name = dependantInfo.getName();
        this.gender = GenderEnum.getDataByCode(dependantInfo.getGender()).getName();
        this.idCard = dependantInfo.getIdCard();
        this.relation = dependantInfo.getRelation();
        this.relationship = dependantInfo.getRelationship();
        this.phone = dependantInfo.getPhone();
        this.institution = dependantInfo.getInstitution();
        this.address = dependantInfo.getAddress();
        this.imageUri = dependantInfo.getImageUri();
        this.createTime = DateTimeUtil.localDateTimeToString(dependantInfo.getCreateTime());
        this.updateTime = DateTimeUtil.localDateTimeToString(dependantInfo.getUpdateTime());
    }
}
