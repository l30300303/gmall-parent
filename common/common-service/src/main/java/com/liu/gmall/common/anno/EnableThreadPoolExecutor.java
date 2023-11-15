package com.liu.gmall.common.anno;
/*
 *@title EnableThreadPoolExecutor
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/14 11:10
 */


import com.liu.gmall.common.config.ThreadPoolExecutorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ThreadPoolExecutorConfiguration.class)
public @interface EnableThreadPoolExecutor {
}
