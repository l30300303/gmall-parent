package com.liu.gmall.common.anno;
/*
 *@title EnableMybatisPlusInterceptor
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/27 19:36
 */


import com.liu.gmall.common.config.MybatisPlusConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MybatisPlusConfiguration.class)
public @interface EnableMybatisPlusInterceptor {
}
