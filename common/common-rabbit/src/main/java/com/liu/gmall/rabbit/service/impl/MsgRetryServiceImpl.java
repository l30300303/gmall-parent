package com.liu.gmall.rabbit.service.impl;
/*
 *@title MsgRetryServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 18:35
 */


import com.liu.gmall.rabbit.service.MsgRetryService;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.io.IOException;

public class MsgRetryServiceImpl implements MsgRetryService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void msgRetry(String msg, long deliveryTag, Channel channel, int count) {
        // 获取消息的实际消费次数，对实际的消费次数进行+1
        String msgMd5 = DigestUtils.md5DigestAsHex(msg.getBytes());
        String msgRedisKey = "msg:count:" + msgMd5;
        Long increment = redisTemplate.opsForValue().increment(msgRedisKey);
        if (increment > count) {
            try {
                redisTemplate.delete(msgRedisKey);
                channel.basicAck(deliveryTag, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
