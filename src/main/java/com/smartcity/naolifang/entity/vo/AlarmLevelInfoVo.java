package com.smartcity.naolifang.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlarmLevelInfoVo {
    private Integer id;
    private Integer alarmLevel;
    private List<Integer> regions = new ArrayList<>();
    private Integer alarmType;
    private List<Integer> malfunctionTypes = new ArrayList<>();
}
