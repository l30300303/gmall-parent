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
import com.liu.gmall.feign.item.SkuInfoDetailFeignClient;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.item.service.SkuInfoDetailService;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuInfoDetailServiceImpl implements SkuInfoDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

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
        if (!bloomFilter.mightContain(skuId)){
            return skuInfoDetailVo;
        }

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

        return skuInfoDetailVo;
    }
}
