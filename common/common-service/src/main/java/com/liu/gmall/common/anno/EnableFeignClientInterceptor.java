package com.liu.gmall.common.anno;
/*
 *@title EnableFeignClientInterceptor
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 22:39
 */

import com.liu.gmall.common.interceptor.FeignClientInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FeignClientInterceptor.class)
public @interface EnableFeignClientInterceptor {
}
