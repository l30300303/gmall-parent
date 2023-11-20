package com.liu.gmall.feign.cart;
/*
 *@title CartFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 20:03
 */


import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.cart.fallback.CartFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-cart", fallback = CartFeignClientFallback.class)
public interface CartFeignClient {

    @GetMapping("/api/inner/cart/addCart")
    public Result<AddCartSuccessVo> addCart(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum);
}
