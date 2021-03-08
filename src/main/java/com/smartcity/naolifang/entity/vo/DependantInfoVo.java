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
    private String coupleName;
    private String institution;
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
        this.coupleName = dependantInfo.getCoupleName();
        this.institution = dependantInfo.getInstitution();
        this.imageUri = dependantInfo.getImageUri();
        this.createTime = DateTimeUtil.localDateTimeToString(dependantInfo.getCreateTime());
        this.updateTime = DateTimeUtil.localDateTimeToString(dependantInfo.getUpdateTime());
    }
}
