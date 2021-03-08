package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.vo.PermissionVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限名称英文
     */
    private String permissionNameEn;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Permission() {
    }

    public Permission(PermissionVo permissionVo) {
        this.id = permissionVo.getId();
        this.permissionName = permissionVo.getPermissionName();
        this.permissionNameEn = permissionVo.getPermissionNameEn();
        this.remark = permissionVo.getRemark();
        this.isDelete = permissionVo.getIsDelete();
        if (StringUtils.isNotBlank(permissionVo.getCreateTime())) {
            this.createTime = LocalDateTime.parse(permissionVo.getCreateTime());
        }
    }
}
