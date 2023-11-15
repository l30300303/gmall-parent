package com.liu.gmall.product.service.impl;
/*
 *@title RestBloomFilterImpl
 *@description  重置布隆过滤器，在删除skuInfo的时候重置布隆过滤器
 *@author L3030
 *@version 1.0
 *@create 2023/11/11 23:13
 */


import com.liu.gmall.product.service.ResetBloomFilter;
import com.liu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
/*import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;*/
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ResetBloomFilterImpl implements ResetBloomFilter {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SkuInfoService skuInfoService;

    @Override
    public void resetBloomFilter() {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("skuIds-bloom-filter-new");
        bloomFilter.tryInit(100000, 0.00001);
        List<Long> allSkuIds = skuInfoService.findAllSkuIds();
        allSkuIds.forEach(id -> bloomFilter.add(id));
        //测试数据，用于区别之前的过滤器
        bloomFilter.add(100L) ;

        // 删除之前的布隆过滤器(skuId-bloom-filter)，对新的布隆过滤器进行重命名
        String script = "redis.call('del' , KEYS[1])\n" +
                "redis.call('del' , \"{\"..KEYS[1]..\"}:config\")\n" +
                "redis.call('rename' , KEYS[2] , KEYS[1])\n" +
                "redis.call('rename' , \"{\"..KEYS[2]..\"}:config\" , \"{\"..KEYS[1]..\"}:config\")\n" +
                "return 1 " ;
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList("skuIds-bloom-filter", "skuIds-bloom-filter-new"));
        if (result == 1){
            log.info("布隆过滤器重置成功！！！");
        }

    }
}
