package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RosterCondition {
    private Integer id;
    private List<Integer> ids = new ArrayList<>();
    private String keyword;
    private String name;
    private String phone;
    private String gender;
    private String groupName;
    private String idCard;
    private String superior;

    private String coupleName;
    private String institution;

    private Integer pageNo;
    private Integer pageSize;
}
