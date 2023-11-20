package com.liu.gmall.common.utils;
/*
 *@title UserAuthUtils
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 21:27
 */


import com.liu.gmall.common.auth.UserAuthInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserAuthUtils {

    public static UserAuthInfo getUserAuthInfo() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        UserAuthInfo userAuthInfo = new UserAuthInfo();
        if (servletRequestAttributes != null) {
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            String userId = httpServletRequest.getHeader("userId");
            String userTempId = httpServletRequest.getHeader("userTempId");
            userAuthInfo.setUserId(userId);
            userAuthInfo.setUserTempId(userTempId);
        }
        return userAuthInfo;
    }
}
