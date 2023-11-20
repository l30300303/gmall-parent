package com.liu.gmall.cart.api;
/*
 *@title CartApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 19:41
 */


import com.liu.gmall.cart.service.CartService;
import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inner/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @GetMapping("/addCart")
    public Result<AddCartSuccessVo> addCart(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum) {
        AddCartSuccessVo addCartSuccessVo = cartService.addCart(skuId, skuNum);
        return Result.ok(addCartSuccessVo);
    }
}
