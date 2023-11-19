package com.liu.gmall.cart;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
