package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.AlarmEventInfo;
import lombok.Data;

@Data
public class AlarmEventInfoVo {
    private Integer id;
    private String content;
    private Integer warningLevel;
    private String indexCode;
    private Integer region;
    private Integer deviceType;
    private String alarmTime;
    private String fixPerson;
    private Integer status;
    private String handlePerson;
    private String handleContent;

    public AlarmEventInfoVo() {
    }

    public AlarmEventInfoVo(AlarmEventInfo alarmEventInfo) {
        this.id = alarmEventInfo.getId();
        this.content = alarmEventInfo.getContent();
        this.warningLevel = alarmEventInfo.getAlarmLevel();
        this.alarmTime = DateTimeUtil.localDateTimeToString(alarmEventInfo.getAlarmTime());
        this.fixPerson = alarmEventInfo.getFixPerson();
        this.status = alarmEventInfo.getStatus();
        this.handlePerson = alarmEventInfo.getHandlePerson();
        this.handlePerson = alarmEventInfo.getHandleContent();
    }
}
