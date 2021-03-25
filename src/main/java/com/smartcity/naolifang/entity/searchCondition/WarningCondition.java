package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WarningCondition {
    private List<Integer> ids = new ArrayList<>();
    private Integer id;
    private String indexCode;
    private Integer alarmLevel;
    private Integer status;
    private String alarmStartTime;
    private String alarmEndTime;
    private Integer alarmType;

    private Integer pageNo;
    private Integer pageSize;
}
