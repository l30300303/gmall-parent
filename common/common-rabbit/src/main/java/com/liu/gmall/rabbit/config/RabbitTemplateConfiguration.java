package com.liu.gmall.rabbit.config;
/*
 *@title RabbitTemplateConfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/23 9:25
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitTemplateConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // 配置RabbitTemplate对象到Spring容器中
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息正常投递到交换机");
            } else {
                log.info("消息投递至交换机失败；case by ：{}", cause);
            }
        });
        // 设置生产者回退机制的回调函数
        rabbitTemplate.setMandatory(true);   // 让rabbitmq把错误信息回传给生产者
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)->{
            log.info("exchange: {} , routingKey: {}" , exchange , routingKey);
        });
        return rabbitTemplate;
    }
}
