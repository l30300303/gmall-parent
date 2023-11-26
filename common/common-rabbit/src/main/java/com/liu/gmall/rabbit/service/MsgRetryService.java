package com.liu.gmall.rabbit.service;
/*
 *@title MsgRetryService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 18:34
 */
import com.rabbitmq.client.Channel;


public interface MsgRetryService {

    void msgRetry(String msg , long deliveryTag , Channel channel , int count) ;

}
