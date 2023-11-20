package com.liu.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.order.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
* @createDate 2023-11-20 21:29:23
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

}




