package com.liu.gmall.common.anno;
/*
 *@title EnableGlobalExceptionHandler
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 19:27
 */


import com.liu.gmall.common.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(GlobalExceptionHandler.class)
public @interface EnableGlobalExceptionHandler {

}
