package com.liu.gmall.order.listener;
/*
 *@title SkuWareStockDeductListener
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/26 17:23
 */


import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.rabbit.service.MsgRetryService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SkuWareStockDeductListener {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MsgRetryService msgRetryService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue.ware.order", durable = "true"),
            exchange = @Exchange(value = "exchange.direct.ware.order", durable = "true", type = ExchangeTypes.DIRECT),
            key = {"ware.order"}
    ))
    public void skuWareStockDeductListener(Message message, Channel channel) {
        //获取消息
        byte[] body = message.getBody();
        String msg = new String(body);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        //进行应答
        try {
            //调用service修改订单状态
            orderInfoService.skuWareStockUpdateStatus(msg);
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
            msgRetryService.msgRetry(msg, deliveryTag, channel, 3);
        }
    }
}
