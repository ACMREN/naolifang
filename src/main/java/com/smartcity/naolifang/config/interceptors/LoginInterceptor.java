package com.smartcity.naolifang.config.interceptors;

import com.smartcity.naolifang.entity.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserVo userVo = (UserVo) request.getSession().getAttribute("user");
        if (null == userVo) {
            return false;
        }
        return true;
    }
}
