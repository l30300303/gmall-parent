package com.liu.gmall.cart.api;
/*
 *@title CartApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 19:41
 */


import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.service.CartService;
import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/deleteCheckedCart")
    public Result deleteCheckedCart(){
        cartService.deleteCheckedCart() ;
        return Result.ok() ;
    }

    @GetMapping(value = "/findByUserId")
    public Result<List<CartItem>> findByUserId() {
        List<CartItem> cartItemList = cartService.findByUserId() ;
        return Result.ok(cartItemList);
    }
}
