package com.liu.gmall.user.dto;
/*
 *@title UserLoginDTO
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 9:08
 */


import lombok.Data;

@Data
public class UserLoginDTO {

    private String loginName;
    private String passwd;
}
