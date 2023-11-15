package com.liu.gmall.common.anno;
/*
 *@title EnableCacheAspect
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/13 21:01
 */


import com.liu.gmall.common.cache.CacheAspect;
import com.liu.gmall.common.service.impl.RedisCacheServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({CacheAspect.class, RedisCacheServiceImpl.class})
public @interface EnableCacheAspect {
}
