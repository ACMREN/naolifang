package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceCondition {
    private List<Integer> ids = new ArrayList<>();
    private String index;
    private String position;
    private String status;
    private String type;
    private String createStartTime;
    private String createEndTime;

    private List<String> indexCodes = new ArrayList<>();
    private String controlType;

    private Integer pageNo;
    private Integer pageSize;
}
