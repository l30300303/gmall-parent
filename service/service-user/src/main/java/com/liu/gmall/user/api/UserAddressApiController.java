package com.liu.gmall.user.api;
/*
 *@title UserApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 20:40
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.user.entity.UserAddress;
import com.liu.gmall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/user")
public class UserAddressApiController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping(value = "/findByUserId")
    public Result<List<UserAddress>> findByUserId() {
        List<UserAddress> userAddressList = userAddressService.findByUserId() ;
        return Result.build(userAddressList , ResultCodeEnum.SUCCESS) ;
    }
}
