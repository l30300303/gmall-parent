package com.liu.gmall.pay.config;
/*
 *@title AlipayConfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:42
 */


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.liu.gmall.pay.properties.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfiguration {

    @Autowired
    private AlipayProperties alipayProperties;

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(
                alipayProperties.getServerUrl(),
                alipayProperties.getAppId(),
                alipayProperties.getPrivateKey(),
                alipayProperties.getFormat(),
                alipayProperties.getCharset(),
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getSignType()
        );
    }
}
