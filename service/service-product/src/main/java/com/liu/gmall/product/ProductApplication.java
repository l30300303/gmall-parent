package com.liu.gmall.product;
/*
 *@title ProductAppliction
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 17:50
 */


import com.liu.gmall.common.anno.EnableFileUploadAutoConfiguration;
import com.liu.gmall.common.anno.EnableThreadPoolExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@MapperScan("com.liu.gmall.product.mapper")
@EnableFileUploadAutoConfiguration
//导入全局异常处理方法一
//@Import(GlobalExceptionHandler.class)
//使用注解的方式导入全局异常处理  方法二
//@EnableGlobalExceptionHandler
@EnableThreadPoolExecutor
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
