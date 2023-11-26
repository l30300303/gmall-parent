package com.liu.gmall.pay.controller;
/*
 *@title PayController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:45
 */


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.liu.gmall.pay.properties.AlipayProperties;
import com.liu.gmall.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/alipay/submit/{orderId}")
    public String pay(@PathVariable(value = "orderId") Long orderId) {
        return payService.pay(orderId);
    }


    @PostMapping(value = "/alipay/listenpayed")
    public String listenPayed(@RequestParam Map<String, String> map) {
        try {
            boolean checkV1 = AlipaySignature.rsaCheckV1(map, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(),alipayProperties.getSignType());
            if (checkV1) {
                log.info("map：{}", JSON.toJSONString(map));
                rabbitTemplate.convertAndSend("payed.exchange","order.payed",JSON.toJSONString(map));
                return "success";
            } else {
                return "error";
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
