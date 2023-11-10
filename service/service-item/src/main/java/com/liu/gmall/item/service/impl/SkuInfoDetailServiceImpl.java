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
import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.item.service.SkuInfoDetailService;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuInfoDetailServiceImpl implements SkuInfoDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private BloomFilter<Long> bloomFilter = BloomFilter.create(Funnels.longFunnel(), 1000000, 0.00001);

    @PostConstruct
    public void initBloomFilter() {
        Result<List<Long>> allSkuIdResult = skuDetailFeignClient.findAllSkuIds();
        List<Long> ids = allSkuIdResult.getData();
        ids.forEach(id -> bloomFilter.put(id));
        log.info("布隆过滤器初始化成功..........");
    }


    @Override
    public SkuInfoDetailVo getSkuInfoDetail(Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = new SkuInfoDetailVo();
        if (!bloomFilter.mightContain(skuId)) {
            return skuInfoDetailVo;
        }

        String infoResult = redisTemplate.opsForValue().get("sku:info:" + skuId);
        if (!StringUtils.isEmpty(infoResult)) {
            log.info("从缓存中获取数据。。。。。。。。");
            return JSON.parseObject(infoResult, SkuInfoDetailVo.class);
        }

        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (lock(uuid, skuId)) {
            try {
                log.info("从数据库中获取数据。。。。。。。。");
                Result<CategoryView> categoryViewResult = skuDetailFeignClient.findCategoryViewBySkuId(skuId);
                CategoryView categoryView = categoryViewResult.getData();
                skuInfoDetailVo.setCategoryView(categoryView);

                Result<SkuInfo> skuInfoResult = skuDetailFeignClient.findSkuInfoAndImageBySkuId(skuId);
                SkuInfo skuInfo = skuInfoResult.getData();
                skuInfoDetailVo.setSkuInfo(skuInfo);

                skuInfoDetailVo.setPrice(skuInfo.getPrice());

                Result<List<SpuSaleAttr>> spuSaleAttrAndSaleAttrValueResult = skuDetailFeignClient.findSpuSaleAttrAndSaleAttrValue(skuId);
                List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrAndSaleAttrValueResult.getData();
                skuInfoDetailVo.setSpuSaleAttrList(spuSaleAttrList);

                Result<List<AttrValueConcatVo>> attrValueConcatBySkuIdResult = skuDetailFeignClient.findAttrValueConcatBySkuId(skuId);
                List<AttrValueConcatVo> attrValueConcatVoList = attrValueConcatBySkuIdResult.getData();
                Map<String, Long> collect = attrValueConcatVoList.stream().collect(Collectors.toMap(attrValueConcatVo -> attrValueConcatVo.getAttrValueConcat(), attrValueConcatVo -> attrValueConcatVo.getSkuId()));
                skuInfoDetailVo.setValuesSkuJson(JSON.toJSONString(collect));
                redisTemplate.opsForValue().set("sku:info:" + skuId, JSON.toJSONString(skuInfoDetailVo));
                return skuInfoDetailVo;
            } catch (Exception e) {
                e.printStackTrace();
                return new SkuInfoDetailVo();  //返回空对象
            } finally {
                unlock(uuid, skuId);
            }
        } else {
            //自旋调用当前方法
            return getSkuInfoDetail(skuId);
        }
    }

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
