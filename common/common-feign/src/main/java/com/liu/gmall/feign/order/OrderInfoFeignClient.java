package com.liu.gmall.feign.order;
/*
 *@title OrderInfoFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 19:36
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.order.fallback.OrderInfoFeignClientFallback;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.vo.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order",fallback = OrderInfoFeignClientFallback.class)
public interface OrderInfoFeignClient {

    @GetMapping(value = "/api/inner/order/trade")
    public Result<OrderConfirmVo> trade();

    @GetMapping("/api/inner/order/findOrderInfoById/{orderId}")
    public Result<OrderInfo> findOrderInfoById(@PathVariable("orderId") Long orderId);

}
