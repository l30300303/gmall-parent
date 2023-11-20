package com.liu.gmall.cart.controller;
/*
 *@title CartController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 22:29
 */


import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.service.CartService;
import com.liu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cartList")
    public Result<List<CartItem>> cartList() {
        List<CartItem> list = cartService.cartList();
        return Result.ok(list);
    }

    @PostMapping("/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId, @PathVariable("skuNum") Integer skuNum) {
        cartService.addToCart(skuId, skuNum);
        return Result.ok();
    }

    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable("skuId") Long skuId, @PathVariable("isChecked") Integer isChecked) {
        cartService.checkCart(skuId, isChecked);
        return Result.ok();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId){
        cartService.deleteCart(skuId);
        return Result.ok();
    }
}
