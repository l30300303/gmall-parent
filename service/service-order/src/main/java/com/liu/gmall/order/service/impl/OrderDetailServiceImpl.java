package com.liu.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.order.entity.OrderDetail;
import com.liu.gmall.order.service.OrderDetailService;
import com.liu.gmall.order.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-11-21 15:47:15
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




