package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.List;

@Data
public class IOManagerCondition {
    private Integer id;
    private String name;
    private String idCard;
    private Integer personType;
    private Integer type; // 出入营类型
    private String startTime;
    private String endTime;
    private String deviceName;
    private List<Integer> insiderIds;
    private List<Integer> dependantIds;
    private List<String> deviceIndexCodes;

    private Integer pageNo;
    private Integer pageSize;
}
