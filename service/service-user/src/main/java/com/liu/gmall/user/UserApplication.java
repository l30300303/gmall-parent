package com.liu.gmall.user;
/*
 *@title UserApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/18 23:10
 */


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.liu.gmall.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
