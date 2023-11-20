package com.liu.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.order.entity.OrderStatusLog;
import com.liu.gmall.order.service.OrderStatusLogService;
import com.liu.gmall.order.mapper.OrderStatusLogMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【order_status_log】的数据库操作Service实现
* @createDate 2023-11-20 21:29:23
*/
@Service
public class OrderStatusLogServiceImpl extends ServiceImpl<OrderStatusLogMapper, OrderStatusLog>
    implements OrderStatusLogService{

}




