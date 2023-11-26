package com.liu.gmall.order.mapper;

import com.liu.gmall.order.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author L3030
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Mapper
 * @createDate 2023-11-20 21:29:23
 * @Entity com.liu.gmall.order.entity.OrderInfo
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    String getOrderStatusById(@Param("orderId") String orderId, @Param("userId") Long userId);
}




