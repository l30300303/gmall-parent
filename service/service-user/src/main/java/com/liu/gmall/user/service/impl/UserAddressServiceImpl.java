package com.liu.gmall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.user.entity.UserAddress;
import com.liu.gmall.user.service.UserAddressService;
import com.liu.gmall.user.mapper.UserAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【user_address(用户地址表)】的数据库操作Service实现
* @createDate 2023-11-19 09:14:32
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

}




