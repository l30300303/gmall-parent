package com.liu.gmall.item.service.impl;
/*
 *@title RedisCacheServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/11 21:25
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.item.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void saveDataToRedis(String key, Object obj) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
    }

    @Override
    public void saveDataToRedis(String key, Object obj, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), time, timeUnit);
    }

    @Override
    public <T> T getDataFromRedis(String key, Class<T> clazz) {
        String result = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return JSON.parseObject(result, clazz);
    }
}
