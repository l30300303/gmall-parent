package com.liu.gmall.common.auth;
/*
 *@title UserAuthInfo
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 21:26
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserAuthInfo {

    private String userId;
    private String userTempId;
}
