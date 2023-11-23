package com.liu.gmall.common.anno;
/*
 *@title EnableFeignClientRetryer
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/23 9:56
 */


import com.liu.gmall.common.retryer.FeignClientRetryer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FeignClientRetryer.class)
public @interface EnableFeignClientRetryer {
}
