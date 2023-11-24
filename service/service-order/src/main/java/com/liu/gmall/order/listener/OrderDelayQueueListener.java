package com.liu.gmall.order.listener;
/*
 *@title OrderDelayQueueListener
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 9:30
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.rabbit.service.MsgRetryService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderDelayQueueListener {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MsgRetryService msgRetryService;

    @RabbitListener(queues = "close_order_queue")
    public void listenerCloseOrderQueue(Message message, Channel channel) {
        byte[] body = message.getBody();
        String msg = new String(body);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            Map<String, String> map = JSON.parseObject(msg, Map.class);
            orderInfoService.closeOrder(Long.valueOf(map.get("orderId")), Long.valueOf(map.get("userId")));
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            e.printStackTrace();
            //当业务出现异常之后，该消息会被重新加入到消息队列，消费者又会获取到这条消息，又处理失败，一直重复这样的操作，就成了死循环
            //设置自定义重试机制
            msgRetryService.msgRetry(msg,deliveryTag,channel,3);
        }
    }
}
