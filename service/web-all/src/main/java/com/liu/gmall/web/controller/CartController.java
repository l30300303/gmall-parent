package com.liu.gmall.web.controller;
/*
 *@title CartController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 19:26
 */


import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.cart.CartFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {


    @Autowired
    private CartFeignClient cartFeignClient;


    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId, @RequestParam("skuNum") Integer skuNum, Model model) {
        Result<AddCartSuccessVo> successVoResult = cartFeignClient.addCart(skuId, skuNum);
        AddCartSuccessVo addCartSuccessVo = successVoResult.getData();

        model.addAttribute("skuInfo", addCartSuccessVo.getSkuInfo());
        model.addAttribute("skuNum", addCartSuccessVo.getSkuNum());
        return "cart/addCart";
    }

    @GetMapping("/cart.html")
    public String cartList(){
        return "cart/index";
    }
}
