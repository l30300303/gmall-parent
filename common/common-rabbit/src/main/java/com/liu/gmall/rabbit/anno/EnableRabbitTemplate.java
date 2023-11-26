package com.liu.gmall.rabbit.anno;
/*
 *@title EnableRabbitTemplate
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/24 18:33
 */


import com.liu.gmall.rabbit.config.RabbitTemplateConfiguration;
import com.liu.gmall.rabbit.service.impl.MsgRetryServiceImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RabbitTemplateConfiguration.class, MsgRetryServiceImpl.class})
public @interface EnableRabbitTemplate {
}
