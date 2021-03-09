package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;

import java.util.List;

@Data
public class UserCondition {
    private Integer id;
    private List<Integer> ids;
    private String keyword;
    private String groupName;
    private String phone;
    private String username;
    private String password;
    private String text;

    private String oldPassword;
    private String newPassword;

    private Integer pageNo;
    private Integer pageSize;
}
