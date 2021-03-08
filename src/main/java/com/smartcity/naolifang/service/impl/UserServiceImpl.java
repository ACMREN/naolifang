package com.smartcity.naolifang.service.impl;

import com.smartcity.naolifang.entity.User;
import com.smartcity.naolifang.mapper.UserMapper;
import com.smartcity.naolifang.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
