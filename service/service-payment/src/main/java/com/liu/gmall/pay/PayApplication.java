package com.liu.gmall.pay;
/*
 *@title PayApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:15
 */


import com.liu.gmall.common.anno.EnableFeignClientInterceptor;
import com.liu.gmall.common.anno.EnableFeignClientRetryer;
import com.liu.gmall.pay.properties.AlipayProperties;
import com.liu.gmall.rabbit.anno.EnableRabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.liu.gmall.feign.order")
@EnableFeignClientInterceptor
@EnableRabbitTemplate
@EnableConfigurationProperties(AlipayProperties.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class);
    }
}
