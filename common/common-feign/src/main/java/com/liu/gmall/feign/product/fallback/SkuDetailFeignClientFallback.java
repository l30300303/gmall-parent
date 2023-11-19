package com.liu.gmall.feign.product.fallback;
/*
 *@title SkuDetailFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/7 18:38
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.vo.AttrValueConcatVo;

import java.util.List;

public class SkuDetailFeignClientFallback implements SkuDetailFeignClient {

    @Override
    public Result<CategoryView> findCategoryViewBySkuId(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<SkuInfo> findSkuInfoAndImageBySkuId(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<List<SpuSaleAttr>> findSpuSaleAttrAndSaleAttrValue(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<List<AttrValueConcatVo>> findAttrValueConcatBySkuId(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<List<Long>> findAllSkuIds() {
        return Result.ok();
    }

    @Override
    public Result<SkuInfoDetailVo> findSkuInfoDetailVo(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<SkuInfo> getSkuInfoById(Long skuId) {
        return Result.ok();
    }
}
