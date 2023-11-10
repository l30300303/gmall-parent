package com.liu.gmall;
/*
 *@title CanalClientApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/10 19:50
 */


import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCanalClient
public class CanalClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanalClientApplication.class, args);
    }

}
