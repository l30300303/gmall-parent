package com.liu.gmall.feign.cart.fallback;
/*
 *@title CartFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 20:04
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.cart.CartFeignClient;
import org.springframework.web.bind.annotation.RequestParam;

public class CartFeignClientFallback implements CartFeignClient {
    @Override
    public Result addCart(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum) {
        return Result.ok();
    }
}
