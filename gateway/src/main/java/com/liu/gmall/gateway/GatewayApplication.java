package com.liu.gmall.gateway;
/*
 *@title GatewayApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 18:00
 */


import com.liu.gmall.gateway.properties.UrlPathProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(UrlPathProperties.class)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
