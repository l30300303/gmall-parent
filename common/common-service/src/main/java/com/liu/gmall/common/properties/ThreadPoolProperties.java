package com.liu.gmall.common.properties;
/*
 *@title ThreadPoolProperties
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/14 9:44
 */


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gmall.threadpool")
public class ThreadPoolProperties {
    private Integer corePoolSize ;
    private Integer maximumPoolSize ;
    private Integer keepAliveTime ;
    private Integer workQueueSize ;
}
