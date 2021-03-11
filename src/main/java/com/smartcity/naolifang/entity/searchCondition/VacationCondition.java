package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.List;

@Data
public class VacationCondition {
    private List<Integer> ids;
    private Integer id;
    private String name;
    private String gender;
    private String leaveStatus;
    private String cancelVacationStatus;

    private Integer pageNo;
    private Integer pageSize;
}
