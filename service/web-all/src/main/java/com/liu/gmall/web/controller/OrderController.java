package com.liu.gmall.web.controller;
/*
 *@title OrderController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 19:20
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.order.OrderInfoFeignClient;
import com.liu.gmall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @GetMapping("/trade.html")
    public String trade(Model model){
        Result<OrderConfirmVo> voResult = orderInfoFeignClient.trade();
        OrderConfirmVo orderConfirmVo = voResult.getData();

        // 从OrderConfirmVo对象获取业务数据将其存储到Model数据模型中
        model.addAttribute("detailArrayList" , orderConfirmVo.getDetailArrayList()) ;
        model.addAttribute("userAddressList" , orderConfirmVo.getUserAddressList()) ;
        model.addAttribute("totalNum" , orderConfirmVo.getTotalNum()) ;
        model.addAttribute("totalAmount" , orderConfirmVo.getTotalAmount()) ;
        model.addAttribute("tradeNo" , orderConfirmVo.getTradeNo()) ;

        return "order/trade" ;
    }

    @GetMapping("/myOrder.html")
    public String toOrder(){
        return "order/myOrder";
    }
}
