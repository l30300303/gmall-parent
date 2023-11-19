package com.liu.gmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.common.constant.RedisConstant;
import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.user.dto.UserLoginDTO;
import com.liu.gmall.user.entity.UserInfo;
import com.liu.gmall.user.service.UserInfoService;
import com.liu.gmall.user.mapper.UserInfoMapper;
import com.liu.gmall.user.vo.UserLoginSuccessVo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author L3030
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2023-11-19 09:14:32
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public UserLoginSuccessVo login(UserLoginDTO userLoginDTO) {
        String loginName = userLoginDTO.getLoginName();
        String password = userLoginDTO.getPasswd();
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            throw new GmallException(ResultCodeEnum.LOGIN_ERROR);
        }

        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInfo::getLoginName, loginName);
        UserInfo info = getOne(lambdaQueryWrapper);
        if (info == null) {
            throw new GmallException(ResultCodeEnum.LOGIN_ERROR);
        }
        //判断密码是否正确

        String digestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!digestAsHex.equals(info.getPasswd())) {
            throw new GmallException(ResultCodeEnum.LOGIN_ERROR);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisConstant.REDIS_USER_LOGIN + token, JSON.toJSONString(info), 7, TimeUnit.DAYS);
        UserLoginSuccessVo userLoginSuccessVo = new UserLoginSuccessVo();
        userLoginSuccessVo.setToken(token);
        userLoginSuccessVo.setNickName(info.getNickName());
        return userLoginSuccessVo;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisConstant.REDIS_USER_LOGIN + token);
    }
}




