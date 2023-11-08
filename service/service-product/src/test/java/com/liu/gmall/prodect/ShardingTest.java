package com.liu.gmall.prodect;
/*
 *@title test
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 21:03
 */


import com.liu.gmall.product.ProductApplication;
import com.liu.gmall.product.entity.SkuImage;
import com.liu.gmall.product.mapper.SkuImageMapper;
import com.liu.gmall.product.mapper.SkuInfoMapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest(classes = ProductApplication.class)
public class ShardingTest {

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Test
    public void findSkuImage() {
        for(int x = 0 ; x < 10 ; x++) {
            SkuImage skuImage = skuImageMapper.selectById(269L);
            System.out.println(skuImage);
        }
    }

}
