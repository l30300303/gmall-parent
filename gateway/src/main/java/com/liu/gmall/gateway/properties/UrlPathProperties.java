package com.liu.gmall.gateway.properties;
/*
 *@title UrlPathProperties
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 11:56
 */


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "gmall.path")
public class UrlPathProperties {

    //不需要登录的资源路径
    private List<String> noauthurls;

    //需要登录验证的资源路径
    private List<String> authurls;

    //登录页面的url
    private String loginpage;
}
