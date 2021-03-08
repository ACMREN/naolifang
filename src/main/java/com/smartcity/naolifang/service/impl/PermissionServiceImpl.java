package com.smartcity.naolifang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.entity.Permission;
import com.smartcity.naolifang.entity.RolePermission;
import com.smartcity.naolifang.mapper.PermissionMapper;
import com.smartcity.naolifang.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.naolifang.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public List<Permission> getPermissionListByRoleId(Integer roleId) {
        List<RolePermission> rolePermissions = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        List<Integer> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissions = this.getBaseMapper().selectBatchIds(permissionIds);
        return permissions;
    }
}
