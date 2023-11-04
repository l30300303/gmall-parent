package com.liu.gmall.common.properties;
/*
 *@title MinioProperties
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 9:18
 */


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gmall.minio")
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
