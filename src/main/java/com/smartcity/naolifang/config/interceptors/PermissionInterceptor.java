package com.smartcity.naolifang.config.interceptors;

import com.smartcity.naolifang.entity.Permission;
import com.smartcity.naolifang.entity.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户是否拥有该权限
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String permissionName = request.getSession().getAttribute("permissionName").toString();
        UserVo userVo = (UserVo) request.getSession().getAttribute("user");
        List<Permission> permissions = userVo.getPermissions();
        for (Permission item : permissions) {
            if (permissionName.equals(item.getPermissionName())) {
                return true;
            }
        }
        return false;
    }
}
