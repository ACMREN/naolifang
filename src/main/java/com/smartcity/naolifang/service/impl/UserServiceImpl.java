package com.smartcity.naolifang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.vo.UserVo;
import com.smartcity.naolifang.mapper.UserMapper;
import com.smartcity.naolifang.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录用户表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public void packageUserRoleAndPermission(UserVo userVo) {
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>()
                .select("role_id")
                .eq("user_id", userVo.getId()));
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleService.getRoleListByUserId(userVo.getId());
            userVo.setRoles(roles);
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>()
                        .select("permission_id")
                        .in("role_id", roleIds));
                List<Integer> permissionIds = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(permissionIds)) {
                    List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
                            .select("id", "permission_name", "permission_name_en")
                            .in("id", permissionIds)
                            .eq("is_delete", 0));
                    userVo.setPermissions(permissions);
                }
            }
        }
    }
}
