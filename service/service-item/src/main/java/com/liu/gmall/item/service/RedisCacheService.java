package com.liu.gmall.item.service;
/*
 *@title RedisCacheService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/11 21:23
 */


import java.util.concurrent.TimeUnit;

public interface RedisCacheService {

    public void saveDataToRedis(String key,Object obj);

    public void saveDataToRedis(String key, Object obj, Long time, TimeUnit timeUnit);

    public <T> T getDataFromRedis(String key,Class<T> clazz);
}
