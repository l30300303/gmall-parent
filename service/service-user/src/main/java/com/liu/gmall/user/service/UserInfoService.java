package com.liu.gmall.user.service;

import com.liu.gmall.user.dto.UserLoginDTO;
import com.liu.gmall.user.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.user.vo.UserLoginSuccessVo;

/**
* @author L3030
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2023-11-19 09:14:32
*/
public interface UserInfoService extends IService<UserInfo> {

    UserLoginSuccessVo login(UserLoginDTO userLoginDTO);

    void logout(String token);
}
