package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.MD5Util;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.vo.*;
import com.smartcity.naolifang.entity.searchCondition.UserCondition;
import com.smartcity.naolifang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private InsiderInfoService insiderInfoService;

    /**
     * 获取验证码
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/getValidateCode")
    public Result getValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ValidateCode code = new ValidateCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        request.getSession().setAttribute("validateCode", text);
        ValidateCode.output(image, response.getOutputStream());
        return Result.ok();
    }

    /**
     * 用户登录
     * @param userCondition
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public Result login(@RequestBody UserCondition userCondition, HttpServletRequest request) {
        String username = userCondition.getUsername();
        String password = userCondition.getPassword();
        String text = userCondition.getText();
        if (null == text) {
            return Result.fail(500, "登录失败，信息：验证码为空");
        }

        // 1. 判断用户是否合法
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (null == user) {
            return Result.fail(500, "登录失败，信息：输入的账号或密码错误");
        }
        String salt = user.getSalt();
        String temp = MD5Util.passwordMd5Encode(password, salt);
        String validateCode = request.getSession().getAttribute("validateCode").toString();
        if (!validateCode.toLowerCase().equals(text.toLowerCase())) {
            return Result.fail(500, "登录失败，信息：验证码错误");
        }
        if (!temp.equals(user.getPassword())) {
            return Result.fail(500, "登录失败，信息：输入的账号或密码错误");
        }

        UserVo userVo = new UserVo(user);

        // 2. 组装用户的角色信息和权限信息
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>()
                .select("role_id")
                .eq("user_id", user.getId()));
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleService.getRoleListByUserId(user.getId());
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

        request.getSession().setAttribute("user", userVo);

        return Result.ok(userVo);
    }

    /**
     * 普通用户获取个人信息
     * @param userCondition
     * @return
     */
    @RequestMapping("/profile/detail")
    public Result getProfileDetail(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        User user = userService.getById(id);

        InsiderInfo insiderInfo = insiderInfoService.getById(user.getRegisterId());

        UserVo userVo = new UserVo(user).packageDetailInfo(insiderInfo);

        return Result.ok(userVo);
    }

    /**
     * 普通用户修改个人信息
     * @param userVo
     * @return
     */
    @RequestMapping("/profile/save")
    public Result saveProfile(@RequestBody UserVo userVo) {
        Integer id = userVo.getId();
        User user = userService.getById(id);


        InsiderInfo insiderInfo = insiderInfoService.getById(user.getRegisterId());
        insiderInfo.updateInsiderInfo(userVo);
        insiderInfoService.saveOrUpdate(insiderInfo);

        return Result.ok();
    }

    /**
     * 密码验证
     * @param userCondition
     * @return
     */
    @RequestMapping("/verifyPassword")
    public Result verifyPassword(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        String password = userCondition.getPassword();

        User user = userService.getById(id);
        String salt = user.getSalt();

        String temp = MD5Util.passwordMd5Encode(password, salt);
        if (temp.equals(user.getPassword())) {
            return Result.ok();
        }
        return Result.fail(500, "密码错误");
    }

    /**
     * 修改密码
     * @param userCondition
     * @return
     */
    @RequestMapping("/changePassword")
    public Result changePassword(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        String oldPassword = userCondition.getOldPassword();
        String newPassword = userCondition.getNewPassword();

        User user = userService.getById(id);
        String salt = user.getSalt();
        String temp = MD5Util.passwordMd5Encode(oldPassword, salt);
        if (!temp.equals(user.getPassword())) {
            return Result.fail(500, "修改密码错误，信息：旧密码输入不正确");
        }

        String newDbPassword = MD5Util.passwordMd5Encode(newPassword, salt);
        user.setPassword(newDbPassword);
        userService.saveOrUpdate(user);

        return Result.ok();
    }

    /**
     * 新增/更新用户
     * @param userVo
     * @return
     */
    @RequestMapping("/account/save")
    public Result saveUser(@RequestBody UserVo userVo) {
        // 1. 保存内部人员信息
        Integer registerId = userVo.getRegisterId();
        InsiderInfo insiderInfo;
        if (null != registerId) {
            insiderInfo = insiderInfoService.getById(registerId);
            insiderInfo.updateInsiderInfo(userVo);
            insiderInfo.setUpdateTime(LocalDateTime.now());
            insiderInfoService.saveOrUpdate(insiderInfo);
        } else {
            insiderInfo = new InsiderInfo();
            insiderInfo.updateInsiderInfo(userVo);
            insiderInfo.setCreateTime(LocalDateTime.now());
            insiderInfo.setUpdateTime(LocalDateTime.now());
            insiderInfoService.saveOrUpdate(insiderInfo);
        }

        // 2. 保存后台用户信息
        User user = new User(userVo);
        if (null == user.getCreateTime()) {
            user.setCreateTime(LocalDateTime.now());
        }
        user.setUpdateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);

        // 3. 处理用户角色关系
        List<Integer> roleIds = userVo.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();
        for (Integer roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        }
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", user.getId()));
        userRoleService.saveOrUpdateBatch(userRoles);

        return Result.ok(user.getId());
    }

    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping("/account/list")
    public Result getUserList(@RequestBody UserCondition userCondition) {
        Integer pageNo = userCondition.getPageNo();
        Integer pageSize = userCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取用户列表失败，传入的页数和页码为空");
        }
        String keyword = userCondition.getKeyword();
        String groupName = userCondition.getGroupName();
        String phone = userCondition.getPhone();
        Integer offset = (pageNo - 1) * pageSize;

        // 1. 查询所有数据
        List<User> userList = userService.list(new QueryWrapper<User>()
                .select("id", "username", "create_time", "update_time")
                .like(StringUtils.isNotBlank(keyword), "username", keyword)
                .eq("is_delete", 0));
        Map<Integer, User> rosterIdUserMap = userList.stream().collect(Collectors.toMap(User::getRegisterId, Function.identity()));
        List<Integer> rosterIds = userList.stream().map(User::getRegisterId).collect(Collectors.toList());
        // 1.1 根据详细数据查询所有的符合条件的用户
        List<InsiderInfo> insiderInfos = insiderInfoService.list(new QueryWrapper<InsiderInfo>()
                .in("id", rosterIds)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(groupName), "group_name", groupName)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = insiderInfoService.count(new QueryWrapper<InsiderInfo>()
                .in("id", rosterIds)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(groupName), "group_name", groupName));

        // 2. 组装所有数据
        List<UserVo> resultList = new ArrayList<>();
        for (InsiderInfo item : insiderInfos) {
            Integer rosterId = item.getId();
            User data = rosterIdUserMap.get(rosterId);
            UserVo userVo = new UserVo(data).packageDetailInfo(item);

            List<Role> roles = roleService.getRoleListByUserId(data.getId());
            userVo.setRoles(roles);

            resultList.add(userVo);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    /**
     * 获取用户详细信息
     * @param userCondition
     * @return
     */
    @RequestMapping("/account/detail")
    public Result getUserDetail(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        User user = userService.getById(id);
        List<Role> roles = roleService.getRoleListByUserId(user.getId());

        UserVo userVo = new UserVo(user);
        userVo.setRoles(roles);

        return Result.ok(userVo);
    }

    /**
     * 删除用户
     * @param userCondition
     * @return
     */
    @RequestMapping("/account/remove")
    public Result deleteUser(@RequestBody UserCondition userCondition) {
        List<Integer> ids = userCondition.getIds();
        List<User> userList = userService.listByIds(ids);
        for (User item : userList) {
            item.setIsDelete(1);
        }
        userService.saveOrUpdateBatch(userList);

        return Result.ok();
    }

    /**
     * 保存用户角色的关系
     * @param userRole
     * @return
     */
    @RequestMapping("/saveUserRole")
    public Result saveUserRole(@RequestBody UserRole userRole) {
        userRoleService.save(userRole);

        return Result.ok();
    }

    /**
     * 删除用户角色关系
     * @param userCondition
     * @return
     */
    @RequestMapping("/deleteUserRole")
    public Result deleteUserRole(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        userRoleService.removeById(id);

        return Result.ok();
    }

    /**
     * 新增/更新角色
     * @param roleVo
     * @return
     */
    @RequestMapping("/role/save")
    public Result saveRole(@RequestBody RoleVo roleVo) {
        Role role = new Role(roleVo);
        if (null == role.getCreateTime()) {
            role.setCreateTime(LocalDateTime.now());
        }
        role.setUpdateTime(LocalDateTime.now());
        roleService.saveOrUpdate(role);

        // 处理角色和权限的关系
        List<Integer> permissionIds = roleVo.getPermissionIds();
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Integer permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", role.getId()));
        rolePermissionService.saveOrUpdateBatch(rolePermissions);

        return Result.ok(role.getId());
    }

    /**
     * 获取角色列表
     * @param userCondition
     * @return
     */
    @RequestMapping("/role/list")
    public Result getRoleList(@RequestBody UserCondition userCondition) {
        Integer pageNo = userCondition.getPageNo();
        Integer pageSize = userCondition.getPageSize();
        String keyword = userCondition.getKeyword();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取用户列表失败，信息：传入的页数和页码为空");
        }
        Integer offset = (pageNo - 1) * pageSize;

        List<Role> dataList = roleService.list(new QueryWrapper<Role>()
                .like(StringUtils.isNotBlank(keyword), "role_name", keyword)
                .eq("is_delete", 0)
                .last("limit " + offset + ", " + pageSize));
        List<RoleVo> resultList = new ArrayList<>();
        Integer totalCount = roleService.count();

        for (Role item : dataList) {
            RoleVo roleVo = new RoleVo(item);

            List<Permission> permissions = permissionService.getPermissionListByRoleId(item.getId());
            roleVo.setPermissions(permissions);

            resultList.add(roleVo);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    /**
     * 删除角色
     * @param userCondition
     * @return
     */
    @RequestMapping("/role/remove")
    public Result deleteRole(@RequestBody UserCondition userCondition) {
        List<Integer> ids = userCondition.getIds();

        for (Integer id : ids) {
            // 1. 先删除这个角色所拥有的权限
            rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", id));

            // 2. 再删除所有拥有这个角色的用户的关系数据
            userRoleService.remove(new QueryWrapper<UserRole>().eq("role_id", id));

            // 3. 再删除这个角色
            Role role = roleService.getById(id);
            role.setIsDelete(1);
            roleService.saveOrUpdate(role);
        }

        return Result.ok();
    }

    /**
     * 保存角色和权限关系
     * @param rolePermission
     * @return
     */
    @RequestMapping("/saveRolePermission")
    public Result saveRolePermission(@RequestBody RolePermission rolePermission) {
        rolePermissionService.save(rolePermission);

        return Result.ok();
    }

    /**
     * 删除角色和和权限关系
     * @param userCondition
     * @return
     */
    @RequestMapping("/deleteRolePermission")
    public Result deleteRolePermission(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        rolePermissionService.removeById(id);

        return Result.ok();
    }

    /**
     * 新增/更新权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("/addPermission")
    public Result addPermission(@RequestBody PermissionVo permissionVo) {
        Permission permission = new Permission(permissionVo);
        if (null == permission.getCreateTime()) {
            permission.setCreateTime(LocalDateTime.now());
        }
        permission.setUpdateTime(LocalDateTime.now());
        permissionService.saveOrUpdate(permission);

        return Result.ok(permission.getId());
    }

    /**
     * 获取权限列表
     * @param userCondition
     * @return
     */
    @RequestMapping("/permission/list")
    public Result getPermissionList(@RequestBody UserCondition userCondition) {
        Integer pageNo = userCondition.getPageNo();
        Integer pageSize = userCondition.getPageSize();
        String keyword = userCondition.getKeyword();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取权限列表失败，信息：传入的页数和页码为空");
        }
        Integer offset = (pageNo - 1) * pageSize;

        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
                .like(StringUtils.isNotBlank(keyword), "permission_name", keyword)
                .eq("is_delete", 0)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = permissionService.count();
        List<PermissionVo> resultList = new ArrayList<>();
        for (Permission item : permissions) {
            PermissionVo permissionVo = new PermissionVo(item);
            resultList.add(permissionVo);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    /**
     * 删除权限
     * @param userCondition
     * @return
     */
    @RequestMapping("/deletePermission")
    public Result deletePermission(@RequestBody UserCondition userCondition) {
        Integer id = userCondition.getId();
        Permission permission = permissionService.getById(id);
        permission.setIsDelete(1);
        permissionService.saveOrUpdate(permission);

        return Result.ok();
    }
}
