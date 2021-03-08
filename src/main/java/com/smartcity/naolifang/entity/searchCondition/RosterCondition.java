package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RosterCondition {
    private Integer id;
    private List<Integer> ids = new ArrayList<>();
    private String keyword;

    private Integer pageNo;
    private Integer pageSize;
}
