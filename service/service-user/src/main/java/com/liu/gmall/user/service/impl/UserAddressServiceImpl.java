package com.liu.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.user.entity.UserAddress;
import com.liu.gmall.user.mapper.UserAddressMapper;
import com.liu.gmall.user.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author L3030
* @description 针对表【user_address(用户地址表)】的数据库操作Service实现
* @createDate 2023-11-19 09:14:32
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

    @Override
    public List<UserAddress> findByUserId() {
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddress::getUserId,userAuthInfo.getUserId());
        return list(lambdaQueryWrapper);
    }
}




