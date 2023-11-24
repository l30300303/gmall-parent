package com.liu.gmall.web.controller;
/*
 *@title PayController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 19:25
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.order.OrderInfoFeignClient;
import com.liu.gmall.order.entity.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PayController {

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @GetMapping(value = "/pay.html")
    public String pay(@RequestParam("orderId") Long orderId, Model model) {
        Result<OrderInfo> infoResult = orderInfoFeignClient.findOrderInfoById(orderId);
        OrderInfo orderInfo = infoResult.getData();
        model.addAttribute("orderInfo", orderInfo);
        return "payment/pay";
    }

    @GetMapping(value = "/success.html")
    public String success() {
        return "payment/success";
    }
}
