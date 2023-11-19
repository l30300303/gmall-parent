package com.liu.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.feign.search.GoodsFeignClient;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.dto.SkuInfoDto;
import com.liu.gmall.product.entity.*;
import com.liu.gmall.product.mapper.*;
import com.liu.gmall.product.service.*;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.entity.SearchAttr;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author L3030
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
@Slf4j
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;


    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Override
    public Page<SkuInfo> getSkuInfoByPage(Integer pageNo, Integer pageSize) {
        Page<SkuInfo> page = new Page<>(pageNo, pageSize);
        return page(page);
    }

    @Override
    public void onSale(Long skuId) {
        //延时双删
        //第一次删除
//        redisTemplate.delete("sku:info:" + skuId);
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 1);
        update(lambdaUpdateWrapper);

        //更新完之后；再次删除
        /*ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.schedule(() -> {
            redisTemplate.delete("sku:info:" + skuId);
        }, 5, TimeUnit.SECONDS);*/

        //请求search远程接口，保存SKu的数据到ES中
        Goods goods = buildGoods(skuId);
        goodsFeignClient.saveGoods(goods);
    }

    private Goods buildGoods(Long skuId) {
        Goods goods = new Goods();
        goods.setId(skuId);
        CompletableFuture<SkuInfo> completableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = getById(skuId);
            goods.setDefaultImg(skuInfo.getSkuDefaultImg());
            goods.setTitle(skuInfo.getSkuName());
            goods.setPrice(skuInfo.getPrice());
            goods.setCreateTime(new Date());
            return skuInfo;
        }, threadPoolExecutor);
        CompletableFuture<Void> cf1 = completableFuture.thenAcceptAsync((skuInfo) -> {
            BaseTrademark baseTrademark = baseTrademarkMapper.selectById(skuInfo.getTmId());
            goods.setTmId(baseTrademark.getId());
            goods.setTmName(baseTrademark.getTmName());
            goods.setTmLogoUrl(baseTrademark.getLogoUrl());
        }, threadPoolExecutor);

        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
            CategoryView categoryView = baseCategory1Service.findCategoryViewBySkuId(skuId);
            goods.setCategory1Id(categoryView.getCategory1Id());
            goods.setCategory1Name(categoryView.getCategory1Name());
            goods.setCategory2Id(categoryView.getCategory2Id());
            goods.setCategory2Name(categoryView.getCategory2Name());
            goods.setCategory3Id(categoryView.getCategory3Id());
            goods.setCategory3Name(categoryView.getCategory3Name());
            //热度分，综合排序使用
            goods.setHotScore(0L);
        }, threadPoolExecutor);
        CompletableFuture<Void> cf3 = CompletableFuture.runAsync(() -> {
            List<SearchAttr> searchAttrList = skuAttrValueMapper.findSkuAttrBySkuId(skuId);
            goods.setAttrs(searchAttrList);
        }, threadPoolExecutor);
        CompletableFuture.allOf(completableFuture, cf1, cf2, cf3).join();
        return goods;
    }

    @Override
    public void cancelSale(Long skuId) {
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 0);
        update(lambdaUpdateWrapper);
         goodsFeignClient.deleteById(skuId);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfoDto skuInfoDto) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoDto, skuInfo);
        skuInfo.setIsSale(0);   //新添加的sku不能直接销售
        save(skuInfo);

        List<SkuImage> skuImageList = skuInfoDto.getSkuImageList();
        skuImageList.stream().forEach(skuImage -> skuImage.setSkuId(skuInfo.getId()));
        skuImageService.saveBatch(skuImageList);

        List<SkuAttrValue> skuAttrValueList = skuInfoDto.getSkuAttrValueList();
        skuAttrValueList.stream().forEach(skuAttrValue -> skuAttrValue.setSkuId(skuInfo.getId()));
        skuAttrValueService.saveBatch(skuAttrValueList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfoDto.getSkuSaleAttrValueList();
        skuSaleAttrValueList.stream().forEach(skuSaleAttrValue -> {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfoDto.getSpuId());
        });
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("skuIds-bloom-filter");
        bloomFilter.add(skuInfo.getId());
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
    }

    @Override
    public SkuInfo findSkuInfoAndImageBySkuId(Long skuId) {
        return skuInfoMapper.findSkuInfoAndImageBySkuId(skuId);
    }

    @Override
    public List<Long> findAllSkuIds() {
        return skuInfoMapper.findAllSkuIds();
    }

    @Override
    public SkuInfoDetailVo findSkuInfoDetailVo(Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = new SkuInfoDetailVo();
        SkuInfo skuInfo = findSkuInfoAndImageBySkuId(skuId);
        if (skuInfo == null) {
            return skuInfoDetailVo;
        }
        skuInfoDetailVo.setSkuInfo(skuInfo);
        skuInfoDetailVo.setPrice(skuInfo.getPrice());

        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
            CategoryView categoryView = baseCategory1Service.findCategoryViewBySkuId(skuId);
            skuInfoDetailVo.setCategoryView(categoryView);
        }, threadPoolExecutor);

        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
            List<SpuSaleAttr> saleAttrValue = spuSaleAttrMapper.findSpuSaleAttrAndSaleAttrValue(skuId);
            skuInfoDetailVo.setSpuSaleAttrList(saleAttrValue);
        }, threadPoolExecutor);

        CompletableFuture<Void> cf3 = CompletableFuture.runAsync(() -> {
            List<AttrValueConcatVo> valueConcatVos = skuSaleAttrValueMapper.findAttrValueConcatBySkuId(skuId);
            Map<String, Long> collect = valueConcatVos.stream().collect(Collectors.toMap(valueConcatVo -> valueConcatVo.getAttrValueConcat(), valueConcatVo -> valueConcatVo.getSkuId()));
            skuInfoDetailVo.setValuesSkuJson(JSON.toJSONString(collect));
        }, threadPoolExecutor);
        CompletableFuture.allOf(cf1, cf2, cf3).join();
        return skuInfoDetailVo;
    }

    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
        return getById(skuId);
    }

    private SkuInfoDetailVo getSkuInfoDetailVoByThreadPool(Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = new SkuInfoDetailVo();
        CountDownLatch countDownLatch = new CountDownLatch(4);
        threadPoolExecutor.submit(() -> {
            CategoryView categoryView = baseCategory1Service.findCategoryViewBySkuId(skuId);
            skuInfoDetailVo.setCategoryView(categoryView);
            countDownLatch.countDown();
        });
        threadPoolExecutor.submit(() -> {
            SkuInfo skuInfo = findSkuInfoAndImageBySkuId(skuId);
            skuInfoDetailVo.setSkuInfo(skuInfo);
            skuInfoDetailVo.setPrice(skuInfo.getPrice());
            countDownLatch.countDown();
        });
        threadPoolExecutor.submit(() -> {
            List<SpuSaleAttr> saleAttrValue = spuSaleAttrMapper.findSpuSaleAttrAndSaleAttrValue(skuId);
            skuInfoDetailVo.setSpuSaleAttrList(saleAttrValue);
            countDownLatch.countDown();
        });

        threadPoolExecutor.submit(() -> {
            List<AttrValueConcatVo> valueConcatVos = skuSaleAttrValueMapper.findAttrValueConcatBySkuId(skuId);
            Map<String, Long> collect = valueConcatVos.stream().collect(Collectors.toMap(valueConcatVo -> valueConcatVo.getAttrValueConcat(), valueConcatVo -> valueConcatVo.getSkuId()));
            skuInfoDetailVo.setValuesSkuJson(JSON.toJSONString(collect));
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
            return skuInfoDetailVo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initBloomFilter() {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("skuIds-bloom-filter");
        bloomFilter.tryInit(100000, 0.0001);
        List<Long> skuIds = findAllSkuIds();
        skuIds.forEach(skuId -> {
            bloomFilter.add(skuId);
        });
        log.info("布隆过滤器初始化成功！！！");
    }
}




