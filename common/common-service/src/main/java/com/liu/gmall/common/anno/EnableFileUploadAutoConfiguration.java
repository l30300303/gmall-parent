package com.liu.gmall.common.anno;
/*
 *@title EnableFileUploadAutoConfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 11:08
 */

import com.liu.gmall.common.config.MinioConfiguration;
import com.liu.gmall.common.service.impl.FileUploadServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({FileUploadServiceImpl.class, MinioConfiguration.class})
public @interface EnableFileUploadAutoConfiguration {
}
