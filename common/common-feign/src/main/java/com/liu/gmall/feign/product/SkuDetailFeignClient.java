package com.liu.gmall.feign.product;
/*
 *@title SkuDetailFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/7 18:38
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.fallback.SkuDetailFeignClientFallback;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(value = "service-product", fallback = SkuDetailFeignClientFallback.class)
public interface SkuDetailFeignClient {

    @GetMapping("/api/inner/product/findCategoryViewBySkuId/{skuId}")
    public Result<CategoryView> findCategoryViewBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/inner/product/findSkuInfoAndImageBySkuId/{skuId}")
    public Result<SkuInfo> findSkuInfoAndImageBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/inner/product/findSpuSaleAttrAndSaleAttrValue/{skuId}")
    public Result<List<SpuSaleAttr>> findSpuSaleAttrAndSaleAttrValue(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/inner/product/findAttrValueConcatBySkuId/{skuId}")
    public Result<List<AttrValueConcatVo>> findAttrValueConcatBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("/api/inner/product/findAllSkuIds")
    public Result<List<Long>> findAllSkuIds();

}
