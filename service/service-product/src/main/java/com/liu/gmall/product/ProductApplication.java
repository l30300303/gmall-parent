package com.liu.gmall.product;
/*
 *@title ProductAppliction
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 17:50
 */


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.liu.gmall.product.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
