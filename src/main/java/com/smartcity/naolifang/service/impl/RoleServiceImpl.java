package com.smartcity.naolifang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.entity.Role;
import com.smartcity.naolifang.entity.UserRole;
import com.smartcity.naolifang.mapper.RoleMapper;
import com.smartcity.naolifang.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcity.naolifang.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Role> getRoleListByUserId(Integer userId) {
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId));
        List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = this.getBaseMapper().selectBatchIds(roleIds);
        return roles;
    }
}
