package com.liu.gmall.common.config;
/*
 *@title MinioConfiguration
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 11:12
 */


import com.liu.gmall.common.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){

        try {
            MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
            }
            return minioClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
