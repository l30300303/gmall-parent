package com.liu.gmall.item.service.impl;
/*
 *@title SkuInfoDetailServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/7 18:36
 */


import com.alibaba.fastjson.JSON;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.liu.gmall.common.anno.GmallCache;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.item.service.RedisCacheService;
import com.liu.gmall.item.service.SkuInfoDetailService;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuInfoDetailServiceImpl implements SkuInfoDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RedissonClient redissonClient;


    /*private BloomFilter<Long> bloomFilter = BloomFilter.create(Funnels.longFunnel(), 1000000, 0.00001);

    @PostConstruct
    public void initBloomFilter() {
        Result<List<Long>> allSkuIdResult = skuDetailFeignClient.findAllSkuIds();
        List<Long> ids = allSkuIdResult.getData();
        ids.forEach(id -> bloomFilter.put(id));
        log.info("布隆过滤器初始化成功..........");
    }*/


    @Override
    @GmallCache(cacheKey = "sku:info:#{ #params[0]}",
            bloomFilterName = "skuIds-bloom-filter",
            bloomFilterValue = "#{#params[0]}",
            enableLock = true,
            lockName = "sku:lock:#{#params[0]}",
            timeout = 30,
            timeUtil = TimeUnit.SECONDS)
    public SkuInfoDetailVo getSkuInfoDetail(Long skuId) {
        Result<SkuInfoDetailVo> skuInfoDetailVoResult = skuDetailFeignClient.findSkuInfoDetailVo(skuId);
        return skuInfoDetailVoResult.getData();

    }

    private SkuInfoDetailVo getInfoDetailVoByClient(Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = new SkuInfoDetailVo();
        Result<SkuInfo> skuInfoResult = skuDetailFeignClient.findSkuInfoAndImageBySkuId(skuId);
        SkuInfo skuInfo = skuInfoResult.getData();
        skuInfoDetailVo.setSkuInfo(skuInfo);
        skuInfoDetailVo.setPrice(skuInfo.getPrice());


        Result<CategoryView> categoryViewResult = skuDetailFeignClient.findCategoryViewBySkuId(skuId);
        CategoryView categoryView = categoryViewResult.getData();
        skuInfoDetailVo.setCategoryView(categoryView);


        Result<List<SpuSaleAttr>> spuSaleAttrAndSaleAttrValueResult = skuDetailFeignClient.findSpuSaleAttrAndSaleAttrValue(skuId);
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrAndSaleAttrValueResult.getData();
        skuInfoDetailVo.setSpuSaleAttrList(spuSaleAttrList);


        Result<List<AttrValueConcatVo>> attrValueConcatBySkuIdResult = skuDetailFeignClient.findAttrValueConcatBySkuId(skuId);
        List<AttrValueConcatVo> attrValueConcatVoList = attrValueConcatBySkuIdResult.getData();
        Map<String, Long> collect = attrValueConcatVoList.stream().collect(Collectors.toMap(attrValueConcatVo -> attrValueConcatVo.getAttrValueConcat(), attrValueConcatVo -> attrValueConcatVo.getSkuId()));
        skuInfoDetailVo.setValuesSkuJson(JSON.toJSONString(collect));


        redisCacheService.saveDataToRedis("sku:info:" + skuId, skuInfoDetailVo);
        //redisTemplate.opsForValue().set( JSON.toJSONString(skuInfoDetailVo));
        return skuInfoDetailVo;
    }

    private SkuInfoDetailVo getSkuInfoDetailVo(Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = new SkuInfoDetailVo();
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("skuIds-bloom-filter");
        if (!bloomFilter.contains(skuId)) {
            return skuInfoDetailVo;
        }

        //String infoResult = redisTemplate.opsForValue().get("sku:info:" + skuId);
        log.info("sku:info:{}", skuId);
        SkuInfoDetailVo detailVo = redisCacheService.getDataFromRedis("sku:info:" + skuId, SkuInfoDetailVo.class);
        if (detailVo != null) {
            log.info("从缓存中获取数据。。。。。。。。");
            return detailVo;
        }

        //String uuid = UUID.randomUUID().toString().replace("-", "");
        RLock lock = redissonClient.getLock("sku:lock:" + skuId);
        if (lock.tryLock()) {
            try {
                log.info("从数据库中获取数据。。。。。。。。");
                Result<SkuInfo> skuInfoResult = skuDetailFeignClient.findSkuInfoAndImageBySkuId(skuId);
                SkuInfo skuInfo = skuInfoResult.getData();
                if (skuInfo == null) {
                    return null;
                }

                Result<CategoryView> categoryViewResult = skuDetailFeignClient.findCategoryViewBySkuId(skuId);
                CategoryView categoryView = categoryViewResult.getData();
                skuInfoDetailVo.setCategoryView(categoryView);


                skuInfoDetailVo.setSkuInfo(skuInfo);

                skuInfoDetailVo.setPrice(skuInfo.getPrice());

                Result<List<SpuSaleAttr>> spuSaleAttrAndSaleAttrValueResult = skuDetailFeignClient.findSpuSaleAttrAndSaleAttrValue(skuId);
                List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrAndSaleAttrValueResult.getData();
                skuInfoDetailVo.setSpuSaleAttrList(spuSaleAttrList);

                Result<List<AttrValueConcatVo>> attrValueConcatBySkuIdResult = skuDetailFeignClient.findAttrValueConcatBySkuId(skuId);
                List<AttrValueConcatVo> attrValueConcatVoList = attrValueConcatBySkuIdResult.getData();
                Map<String, Long> collect = attrValueConcatVoList.stream().collect(Collectors.toMap(attrValueConcatVo -> attrValueConcatVo.getAttrValueConcat(), attrValueConcatVo -> attrValueConcatVo.getSkuId()));
                skuInfoDetailVo.setValuesSkuJson(JSON.toJSONString(collect));
                redisCacheService.saveDataToRedis("sku:info:" + skuId, skuInfoDetailVo);
                //redisTemplate.opsForValue().set( JSON.toJSONString(skuInfoDetailVo));
                return skuInfoDetailVo;
            } catch (Exception e) {
                e.printStackTrace();
                return new SkuInfoDetailVo();
            } finally {
                lock.unlock();
            }
        } else {
            //自旋调用当前方法
            return getSkuInfoDetail(skuId);
        }
    }

    //自定义redis分布式锁加锁方法
    public Boolean lock(String uuid, Long skuId) {
        Boolean absent = redisTemplate.opsForValue().setIfAbsent("sku:lock:" + skuId, uuid, 10, TimeUnit.SECONDS);
        if (absent) {
            Thread thread = new Thread(() -> {
                // 开启一个周期性执行任务，对锁进行续期
                ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
                scheduledThreadPool.scheduleAtFixedRate(() -> {
                    redisTemplate.expire("sku:lock:" + skuId, 10, TimeUnit.SECONDS);
                    // 5表示第一次执行任务是的延迟时间、10表示的后续执行任务时所对应的时间间隔
                }, 5, 10, TimeUnit.SECONDS);
            });
            thread.setDaemon(true);
            thread.start();
        }
        return absent;
    }

    //自定义redis分布式锁释放锁方法
    public void unlock(String uuid, Long skuId) {
        String script = "if redis.call(\"get\" , KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList("sku:lock:" + skuId), uuid);
        if (execute == 1) {
            log.info("锁已释放。。。。。");
        } else {
            log.info("锁释放失败。。。。。");
        }

    }
}
