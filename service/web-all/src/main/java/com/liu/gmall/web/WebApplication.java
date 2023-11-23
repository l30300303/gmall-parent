package com.liu.gmall.web;
/*
 *@title WebApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 18:08
 */


import com.liu.gmall.common.anno.EnableFeignClientInterceptor;
import com.liu.gmall.common.anno.EnableFeignClientRetryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = {
        "com.liu.gmall.feign.product",
        "com.liu.gmall.feign.item",
        "com.liu.gmall.feign.search",
        "com.liu.gmall.feign.cart",
        "com.liu.gmall.feign.order"
})
@EnableFeignClientInterceptor
@EnableFeignClientRetryer
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
