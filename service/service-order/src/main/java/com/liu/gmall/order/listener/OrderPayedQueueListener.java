package com.liu.gmall.order.listener;
/*
 *@title OrderPayedQueueListener
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/26 10:25
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.rabbit.service.MsgRetryService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OrderPayedQueueListener {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MsgRetryService msgRetryService;

    @RabbitListener(queues = "order.payed.queue")
    public void orderPayedQueueListener(Message message, Channel channel) {
        byte[] body = message.getBody();
        String msg = new String(body);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //对消息进行手动应答
        try {
            Map<String, String> map = JSON.parseObject(msg, Map.class);
            //修改订单状态
            orderInfoService.orderPayedUpdateOrderStatus(map);
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
            msgRetryService.msgRetry(msg, deliveryTag, channel, 3);
        }

    }
}
