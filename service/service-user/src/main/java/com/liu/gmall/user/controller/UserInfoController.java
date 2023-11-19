package com.liu.gmall.user.controller;
/*
 *@title UserInfoController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/18 23:11
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.user.dto.UserLoginDTO;
import com.liu.gmall.user.service.UserInfoService;
import com.liu.gmall.user.vo.UserLoginSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user/passport")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public Result<UserLoginSuccessVo> login(@RequestBody UserLoginDTO userLoginDTO){
        UserLoginSuccessVo loginSuccessVo = userInfoService.login(userLoginDTO);
        return Result.ok(loginSuccessVo);
    }

    @GetMapping("/logout")
    public Result logout(@RequestHeader("token") String token){
        userInfoService.logout(token);
        return Result.ok();
    }

}
