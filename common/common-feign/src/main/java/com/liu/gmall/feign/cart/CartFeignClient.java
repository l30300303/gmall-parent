package com.liu.gmall.feign.cart;
/*
 *@title CartFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 20:03
 */


import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.cart.fallback.CartFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-cart", fallback = CartFeignClientFallback.class)
public interface CartFeignClient {

    @GetMapping("/api/inner/cart/addCart")
    public Result<AddCartSuccessVo> addCart(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum);

    @DeleteMapping("/api/inner/cart/deleteCheckedCart")
    public Result deleteCheckedCart();

    @GetMapping(value = "/api/inner/cart/findByUserId")
    public Result<List<CartItem>> findByUserId();
}
