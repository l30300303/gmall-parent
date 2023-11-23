package com.liu.gmall.feign.order.fallback;
/*
 *@title OrderInfoFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 19:37
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.order.OrderInfoFeignClient;
import com.liu.gmall.order.vo.OrderConfirmVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderInfoFeignClientFallback implements OrderInfoFeignClient {
    @Override
    public Result<OrderConfirmVo> trade() {
        log.info("执行OrderInfoFeignClientFallback。。。。。trade 的降级方法");
        return Result.ok();
    }
}
