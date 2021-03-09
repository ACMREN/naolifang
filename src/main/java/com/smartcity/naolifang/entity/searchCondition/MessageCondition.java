package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageCondition {
    private List<Integer> ids = new ArrayList<>();
    private Integer newInterval;

    private Integer pageNo;
    private Integer pageSize;
}
