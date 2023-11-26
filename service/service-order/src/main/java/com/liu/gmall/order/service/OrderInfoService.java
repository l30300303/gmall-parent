package com.liu.gmall.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.order.vo.OrderConfirmVo;
import com.liu.gmall.ware.entity.WareStockMsg;

import java.util.List;
import java.util.Map;

/**
 * @author L3030
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service
 * @createDate 2023-11-20 21:29:23
 */
public interface OrderInfoService extends IService<OrderInfo> {

    OrderConfirmVo trade();

    String submitOrder(String tradeNo, OrderSubmitDto orderSubmitDTO);

    Page<OrderInfo> getOrder(Integer pageNum, Integer pageSize);

    void closeOrder(Long orderId,Long userId);

    OrderInfo findOrderInfoById(Long orderId);

    void orderPayedUpdateOrderStatus(Map<String, String> map);

    void skuWareStockUpdateStatus(String msg);

    List<WareStockMsg> orderSplit(Long orderId, String wareSkuMap);
}
