package com.liu.gmall.admin;
/*
 *@title AdminApplication
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/8 18:15
 */


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
