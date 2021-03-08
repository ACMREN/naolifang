package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DutyCondition {
    private List<Integer> ids = new ArrayList<>();
    private String name;
    private String gender;
    private String position;
    private String startTime;
    private String endTime;

    private Integer pageNo;
    private Integer pageSize;
}
