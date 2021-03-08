package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.Permission;
import lombok.Data;

@Data
public class PermissionVo {
    private Integer id;
    private String permissionName;
    private String permissionNameEn;
    private String remark;
    private Integer isDelete;
    private String createTime;
    private String updateTime;

    public PermissionVo() {
    }

    public PermissionVo(Permission permission) {
        this.id = permission.getId();
        this.permissionName = permission.getPermissionName();
        this.permissionNameEn = permission.getPermissionNameEn();
        this.remark = permission.getRemark();
        this.createTime = DateTimeUtil.localDateTimeToString(permission.getCreateTime());
        this.updateTime = DateTimeUtil.localDateTimeToString(permission.getUpdateTime());
    }
}
