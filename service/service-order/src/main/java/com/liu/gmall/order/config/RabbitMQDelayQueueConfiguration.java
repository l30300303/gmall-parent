package com.liu.gmall.order.config;
/*
 *@title RabbitMQDelayQueueConfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 8:48
 */


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDelayQueueConfiguration {

    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.directExchange("order.exchange").durable(true).build();
    }


    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order_queue")
                .ttl(1000 * 60 * 30)
                .deadLetterExchange("order.exchange")
                .deadLetterRoutingKey("close.order")
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.info").noargs();
    }

    @Bean
    public Queue closeOrderQueue() {
        return QueueBuilder.durable("close_order_queue").build();
    }

    @Bean
    public Binding closeOrderBinding() {
        return BindingBuilder.bind(closeOrderQueue()).to(orderExchange()).with("close.order").noargs();
    }
}
