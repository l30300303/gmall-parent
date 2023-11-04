package com.liu.gmall.prodect;
/*
 *@title test
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 21:03
 */


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.File;
import java.io.FileInputStream;

public class MinioTest {

    public static void main(String[] args) throws Exception {
        try {

            // 创建minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://192.168.126.138:9000")
                    .credentials("liubaiqi", "12345678")
                    .build();

            // 判断bucket是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("gmall").build());
            if (!found) {

                // 创建一个bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("gmall").build());

            }

            // 上传文件
            FileInputStream fileInputStream = new FileInputStream("D://images//1.png") ;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("gmall")
                    .object("1.png")            // 对象名称
                    .stream(fileInputStream, fileInputStream.available() , -1)
                    .build()) ;

            // 获取访问地址


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
