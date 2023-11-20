package com.liu.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.order.entity.CartInfo;
import com.liu.gmall.order.service.CartInfoService;
import com.liu.gmall.order.mapper.CartInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【cart_info(购物车表 用户登录系统时更新冗余)】的数据库操作Service实现
* @createDate 2023-11-20 21:29:23
*/
@Service
public class CartInfoServiceImpl extends ServiceImpl<CartInfoMapper, CartInfo>
    implements CartInfoService{

}




