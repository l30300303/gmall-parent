package com.liu.gmall.common.service;
/*
 *@title RedisCacheService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 21:00
 */


import java.util.concurrent.TimeUnit;
import java.lang.reflect.Type;

public interface RedisCacheService {

    // 把数据存储到Redis中的方法
    public abstract void saveDataToRedis(String key , Object obj , long timeout , TimeUnit timeUnit)  ;

    // 从Redis中获取数据
    public abstract <T> T getDataFromRedis(String key , Class<T> clazz) ;

    public abstract <T> T getDataFromRedis(String key , Type type) ;

}
