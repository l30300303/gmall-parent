package com.liu.gmall.pay.controller;
/*
 *@title PayController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:45
 */


import com.liu.gmall.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping(value = "/alipay/submit/{orderId}")
    public String pay(@PathVariable(value = "orderId") Long orderId) {
        return payService.pay(orderId) ;
    }
}
