package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceCondition {
    private List<Integer> ids = new ArrayList<>();
    private String indexCode;
    private Integer region;
    private Integer status;
    private String type;
    private String connectStartTime;
    private String connectEndTime;

    private List<String> indexCodes = new ArrayList<>();
    private Integer controlType;
    private Integer changeStatus;

    private List<Integer> eventTypes = new ArrayList<>();
    private String callBackApi;

    private Integer pageNo;
    private Integer pageSize;
}
