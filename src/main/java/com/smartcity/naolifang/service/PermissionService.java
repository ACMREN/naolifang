package com.smartcity.naolifang.service;

import com.smartcity.naolifang.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getPermissionListByRoleId(Integer roleId);

}
