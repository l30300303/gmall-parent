package com.liu.gmall.common.cache;
/*
 *@title CacheAspect
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 20:26
 */


import com.liu.gmall.common.anno.GmallCache;
import com.liu.gmall.common.service.RedisCacheService;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class CacheAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisCacheService redisCacheService;

    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Around("@annotation(gmallCache)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, GmallCache gmallCache) {
        Object skuId = proceedingJoinPoint.getArgs()[0];

        //获取目标方法的返回值类型
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Type returnType = method.getGenericReturnType();

        //布隆过滤器
        String bloomFilterNameExpression = gmallCache.bloomFilterName();
        String bloomFilterValueExpression = gmallCache.bloomFilterValue();
        if (!StringUtils.isEmpty(bloomFilterNameExpression) && !StringUtils.isEmpty(bloomFilterValueExpression)) {
            String bloomFilterName = parseSpelExpression(bloomFilterNameExpression, proceedingJoinPoint, String.class);
            Object bloomFilterValue = parseSpelExpression(bloomFilterValueExpression, proceedingJoinPoint, Object.class);
            RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
            if (!bloomFilter.contains(bloomFilterValue)) {
                log.info("布隆过滤器判断没有数据，直接返回空对象，skuId: {}", skuId);
                try {
                    Class<?> type = method.getReturnType();
                    return type.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //查询数据
        String cacheExpression = gmallCache.cacheKey();
        String cacheKey = parseSpelExpression(cacheExpression, proceedingJoinPoint, String.class);
        Object result = redisCacheService.getDataFromRedis(cacheKey, returnType);
        if (result != null) {
            log.info("从缓存中查询到了数据。。。。");
            return result;
        }
        boolean enableLock = gmallCache.enableLock();
        String lockNameExpression = gmallCache.lockName();
        long timeout = gmallCache.timeout();
        TimeUnit timeUnit = gmallCache.timeUtil();
        if (enableLock && !StringUtils.isEmpty(lockNameExpression)) {
            String lockName = parseSpelExpression(lockNameExpression, proceedingJoinPoint, String.class);
            RLock lock = redissonClient.getLock(lockName);
            if (lock.tryLock()) {
                try {
                    log.info("获取到分布式锁，查询数据库");
                    result = proceedingJoinPoint.proceed();
                    //将数据存储到redis中
                    redisCacheService.saveDataToRedis(cacheKey, result, timeout, timeUnit);
                    log.info("将数据库中查询的数据存储到redis中");

                    return result;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                    log.info("释放分布式锁----" + Thread.currentThread().getName());
                }
            } else {
                return around(proceedingJoinPoint, gmallCache);
            }
        } else {    //不需要使用分布式锁，直接查询数据库
            try {
                result = proceedingJoinPoint.proceed();
                log.info("获取到分布式锁，查询数据库");
                redisCacheService.saveDataToRedis(cacheKey, result, timeout, timeUnit);
                log.info("获取到分布式锁，查询数据库");
                return result;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    //对表达式进行解析
    private <T> T parseSpelExpression(String cacheExpression, ProceedingJoinPoint proceedingJoinPoint, Class<T> calzz) {
        Expression expression = spelExpressionParser.parseExpression(cacheExpression, ParserContext.TEMPLATE_EXPRESSION);
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("params", proceedingJoinPoint.getArgs());
        T value = expression.getValue(standardEvaluationContext, calzz);
        return value;
    }
}
