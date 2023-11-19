package com.liu.gmall.common.interceptor;
/*
 *@title FeignClientInterceptor
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 20:17
 */


import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.utils.UserAuthUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        template.header("userId", userAuthInfo.getUserId());
        template.header("userTempId", userAuthInfo.getUserTempId());
    }
}
