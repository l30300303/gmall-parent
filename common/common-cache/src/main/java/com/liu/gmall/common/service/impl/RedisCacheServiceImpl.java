package com.liu.gmall.common.service.impl;
/*
 *@title RedisCacheServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 21:00
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.common.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void saveDataToRedis(String key, Object obj, long timeout, TimeUnit timeUnit) {
        String json = JSON.toJSONString(obj);       // "null"
        redisTemplate.opsForValue().set(key , json , timeout , timeUnit) ;
    }

    @Override
    public <T> T getDataFromRedis(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(json)) {
            return null ;
        }else {
            return JSON.parseObject(json , clazz);
        }
    }

    @Override
    public <T> T getDataFromRedis(String key, Type type) {
        String json = redisTemplate.opsForValue().get(key);  // "null"
        if(StringUtils.isEmpty(json)) {
            return null ;
        }else {
            return JSON.parseObject(json , type) ;
        }
    }
}
