package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

@Data
public class IOManagerCondition {
    private Integer id;
    private String name;
    private String idCard;
    private Integer personType;
    private String deviceName;

    private Integer pageNo;
    private Integer pageSize;
}
