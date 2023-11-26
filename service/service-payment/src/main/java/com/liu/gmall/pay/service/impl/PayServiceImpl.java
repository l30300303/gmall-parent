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
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.enums.OrderStatus;
import com.liu.gmall.enums.ProcessStatus;
import com.liu.gmall.feign.order.OrderInfoFeignClient;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.pay.properties.AlipayProperties;
import com.liu.gmall.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        //判断订单状态是否已关闭
        if (OrderStatus.CLOSED.name().equals(orderInfo.getOrderStatus()) && ProcessStatus.CLOSED.name().equals(orderInfo.getProcessStatus())) {
            throw new GmallException(ResultCodeEnum.ORDER_PAY_ERROR);
        }
        AlipayTradePagePayRequest tradePagePayRequest = new AlipayTradePagePayRequest();
        tradePagePayRequest.setReturnUrl(alipayProperties.getReturnUrl());
        tradePagePayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
        // 构建一个业务的json数据
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        map.put("total_amount", orderInfo.getTotalAmount());
        map.put("subject", orderInfo.getTradeBody());
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(3);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = dateTimeFormatter.format(localDateTime);
        map.put("time_expire", format);
        tradePagePayRequest.setBizContent(JSON.toJSONString(map));
        try {
            // 发送请求
            return alipayClient.pageExecute(tradePagePayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
