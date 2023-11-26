package com.liu.gmall.pay.config;
/*
 *@title OrderPayedRabbitCOnfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/26 10:07
 */


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderPayedRabbitConfiguration {

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("payed.exchange").durable(true).build();
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable("order.payed.queue").build();
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with("order.payed").noargs();
    }
}
