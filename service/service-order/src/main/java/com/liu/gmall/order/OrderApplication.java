package com.liu.gmall.order;
/*
 *@title OrderApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/20 21:06
 */


import com.liu.gmall.common.anno.EnableFeignClientInterceptor;
import com.liu.gmall.common.anno.EnableMybatisPlusInterceptor;
import com.liu.gmall.common.anno.EnableThreadPoolExecutor;
import com.liu.gmall.rabbit.anno.EnableRabbitTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableThreadPoolExecutor
@EnableFeignClients(basePackages = {
        "com.liu.gmall.feign.cart",
        "com.liu.gmall.feign.user",
        "com.liu.gmall.feign.product",
        "com.liu.gmall.feign.ware"
})
@MapperScan(basePackages = "com.liu.gmall.order.mapper")
@EnableFeignClientInterceptor
@EnableRabbitTemplate
@EnableMybatisPlusInterceptor
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
