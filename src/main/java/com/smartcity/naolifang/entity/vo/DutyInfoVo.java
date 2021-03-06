package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.DutyInfo;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import lombok.Data;

@Data
public class DutyInfoVo {
    private Integer id;
    private String name;
    private String gender;
    private Integer insiderId;
    private String position;
    private String nickName;
    private String groupName;
    private String imageUri;
    private String startTime;
    private String endTime;

    public DutyInfoVo(DutyInfo dutyInfo) {
        this.id = dutyInfo.getId();
        this.name = dutyInfo.getName();
        this.gender = GenderEnum.getDataByCode(dutyInfo.getGender()).getName();
        this.insiderId = dutyInfo.getInsiderId();
        this.position = dutyInfo.getPosition();
        this.nickName = dutyInfo.getNickName();
        this.groupName = dutyInfo.getGroupName();
        this.startTime = DateTimeUtil.localDateTimeToString(dutyInfo.getStartTime());
        this.endTime = DateTimeUtil.localDateTimeToString(dutyInfo.getEndTime());
    }

    public DutyInfoVo() {
    }
}
