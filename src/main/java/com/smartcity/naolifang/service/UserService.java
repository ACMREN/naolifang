package com.smartcity.naolifang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcity.naolifang.entity.User;
import com.smartcity.naolifang.entity.vo.UserVo;

/**
 * <p>
 * 登录用户表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
public interface UserService extends IService<User> {

    void packageUserRoleAndPermission(UserVo userVo);

}
