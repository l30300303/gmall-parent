package com.liu.gmall.pay.service.impl;
/*
 *@title PayServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 20:44
 */


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePageMergePayRequest;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.order.OrderInfoFeignClient;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.pay.properties.AlipayProperties;
import com.liu.gmall.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public String pay(Long orderId) {
        Result<OrderInfo> orderInfoResult = orderInfoFeignClient.findOrderInfoById(orderId);
        OrderInfo orderInfo = orderInfoResult.getData();
        AlipayTradePageMergePayRequest alipayTradePageMergePayRequest = new AlipayTradePageMergePayRequest();
        alipayTradePageMergePayRequest.setReturnUrl(alipayProperties.getReturnUrl());
        alipayTradePageMergePayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
        // 构建一个业务的json数据
        Map<String , Object> map = new HashMap<>() ;
        map.put("out_trade_no" , orderInfo.getOutTradeNo()) ;
        map.put("total_amount" , orderInfo.getTotalAmount()) ;
        map.put("subject" , orderInfo.getTradeBody()) ;
        map.put("product_code" , "FAST_INSTANT_TRADE_PAY") ;
        alipayTradePageMergePayRequest.setBizContent(JSON.toJSONString(map)) ;
        try {
            // 发送请求
            return alipayClient.pageExecute(alipayTradePageMergePayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
