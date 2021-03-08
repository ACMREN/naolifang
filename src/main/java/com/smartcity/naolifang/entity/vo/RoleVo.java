package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.entity.Permission;
import com.smartcity.naolifang.entity.Role;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoleVo {
    private Integer id;
    private String roleName;
    private String remark;
    private Integer isDelete;
    private String createTime;
    private String updateTime;

    private List<Integer> permissionIds = new ArrayList<>();
    private List<Permission> permissions = new ArrayList<>();

    public RoleVo() {
    }

    public RoleVo(Role role) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.id = role.getId();
        this.roleName = role.getRoleName();
        this.remark = role.getRemark();
        this.createTime = dtf.format(role.getCreateTime());
        this.updateTime = dtf.format(role.getUpdateTime());
    }
}
