package com.liu.gmall.pay.properties;
/*
 *@title AlipayProperties
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:19
 */


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gmall.alipay")
public class AlipayProperties {
    private String serverUrl;
    private String appId;
    private String privateKey;
    private String format;
    private String charset;
    private String alipayPublicKey;
    private String signType;
    private String notifyUrl;
    private String returnUrl;
}
