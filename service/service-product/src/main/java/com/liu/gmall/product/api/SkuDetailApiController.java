package com.liu.gmall.product.api;
/*
 *@title SkuDetailApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/7 19:49
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.service.BaseCategory1Service;
import com.liu.gmall.product.service.SkuInfoService;
import com.liu.gmall.product.service.SkuSaleAttrValueService;
import com.liu.gmall.product.service.SpuSaleAttrService;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/product")
public class SkuDetailApiController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @GetMapping("/findCategoryViewBySkuId/{skuId}")
    public Result<CategoryView> findCategoryViewBySkuId(@PathVariable("skuId") Long skuId) {
        CategoryView categoryView = baseCategory1Service.findCategoryViewBySkuId(skuId);
        return Result.ok(categoryView);
    }

    @GetMapping("/findSkuInfoAndImageBySkuId/{skuId}")
    public Result<SkuInfo> findSkuInfoAndImageBySkuId(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.findSkuInfoAndImageBySkuId(skuId);
        return Result.ok(skuInfo);
    }

    @GetMapping("/findSpuSaleAttrAndSaleAttrValue/{skuId}")
    public Result<List<SpuSaleAttr>> findSpuSaleAttrAndSaleAttrValue(@PathVariable("skuId") Long skuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.findSpuSaleAttrAndSaleAttrValue(skuId);
        return Result.ok(spuSaleAttrList);
    }

    @GetMapping("/findAttrValueConcatBySkuId/{skuId}")
    public Result<List<AttrValueConcatVo>> findAttrValueConcatBySkuId(@PathVariable("skuId") Long skuId) {
        List<AttrValueConcatVo> attrValueConcatVoList = skuSaleAttrValueService.findAttrValueConcatBySkuId(skuId);
        return Result.ok(attrValueConcatVoList);
    }

    @GetMapping("/findAllSkuIds")
    public Result<List<Long>> findAllSkuIds() {
        List<Long> ids = skuInfoService.findAllSkuIds();
        return Result.ok(ids);
    }

    @GetMapping("/findSkuInfoDetailVo/{skuId}")
    public Result<SkuInfoDetailVo> findSkuInfoDetailVo(@PathVariable("skuId") Long skuId) {
        SkuInfoDetailVo skuInfoDetailVo = skuInfoService.findSkuInfoDetailVo(skuId);
        return Result.ok(skuInfoDetailVo);
    }

    @GetMapping("/getSkuInfoById/{skuId}")
    public Result<SkuInfo> getSkuInfoById(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.getSkuInfoById(skuId);
        return Result.ok(skuInfo);
    }
}
