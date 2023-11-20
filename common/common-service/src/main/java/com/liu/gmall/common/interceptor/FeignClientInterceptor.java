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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        log.info("执行feign拦截器：：：进行用户id透传");
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        log.info("userId:::{},....userTempId:::{}",userAuthInfo.getUserId(), userAuthInfo.getUserTempId());
        template.header("userId", userAuthInfo.getUserId());
        template.header("userTempId", userAuthInfo.getUserTempId());
    }
}
