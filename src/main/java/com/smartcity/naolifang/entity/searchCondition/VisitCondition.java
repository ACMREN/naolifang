package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VisitCondition {
    private List<Integer> ids = new ArrayList<>();
    private Integer visitorId;
    private Integer id;
    private String name;
    private String idCard;
    private String phone;
    private Integer status;

    private Integer pageNo;
    private Integer pageSize;
}
