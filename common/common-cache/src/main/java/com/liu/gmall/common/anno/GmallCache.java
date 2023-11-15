package com.liu.gmall.common.anno;
/*
 *@title GmallCache
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 20:29
 */


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GmallCache {

    public String cacheKey();  // 缓存数据的key

    public String bloomFilterName() default "";  // 布隆过滤器的名称

    public String bloomFilterValue() default "";  // 布隆过滤器需要判断的数据

    public boolean enableLock() default false;  // 是否需要启用分布式锁

    public String lockName() default "";    //  分布式锁的名称

    public long timeout() default 60;   //  缓存默认过期时间

    public TimeUnit timeUtil() default TimeUnit.SECONDS;    //  过期时间单位

}
