package com.liu.gmall.order.api;
/*
 *@title OrderInfoApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 19:31
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inner/order")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping(value = "/trade")
    public Result<OrderConfirmVo> trade() {
        OrderConfirmVo orderConfirmVo = orderInfoService.trade();
        return Result.ok(orderConfirmVo);
    }

    @GetMapping("/findOrderInfoById/{orderId}")
    public Result<OrderInfo> findOrderInfoById(@PathVariable("orderId") Long orderId) {
        OrderInfo orderInfo = orderInfoService.findOrderInfoById(orderId);
        return Result.ok(orderInfo);
    }
}
