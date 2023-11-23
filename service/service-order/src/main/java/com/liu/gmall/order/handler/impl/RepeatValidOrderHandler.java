package com.liu.gmall.order.handler.impl;
/*
 *@title RepeatValidOrderHandler
 *@description  重复提交校验
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:25
 */


import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.handler.AbstractOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Component
public class RepeatValidOrderHandler extends AbstractOrderHandler {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public int sort() {
        return 1;
    }

    @Override
    public String process(OrderSubmitDto orderSubmitDto, String tradeNo) {
        String script = "if redis.call('exists',KEYS[1])" +
                "then" +
                "   return redis.call('del',KEYS[1])" +
                "else" +
                "   return 0 \n" +
                "end";
        //Collections.singletonList被限定只能放一个元素，多了少了都会报错（节省空间）
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("order:info:tradeno:" + tradeNo));
        if (execute == 0) {
            throw new GmallException(ResultCodeEnum.ORDER_FORM_REPEAT);
        }
        AbstractOrderHandler next = getNext();
        if (next != null) {
            return next.process(orderSubmitDto, tradeNo);
        }
        return null;
    }
}
