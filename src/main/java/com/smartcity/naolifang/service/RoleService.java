package com.smartcity.naolifang.service;

import com.smartcity.naolifang.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
public interface RoleService extends IService<Role> {

    List<Role> getRoleListByUserId(Integer userId);

}
