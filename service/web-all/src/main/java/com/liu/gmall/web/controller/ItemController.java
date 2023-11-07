package com.liu.gmall.web.controller;
/*
 *@title ItemController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 21:22
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.item.SkuInfoDetailFeignClient;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    private SkuInfoDetailFeignClient skuInfoDetailFeignClient;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model) {
        Result<SkuInfoDetailVo> skuInfoDetailVoResult = skuInfoDetailFeignClient.getSkuInfoDetail(skuId);
        SkuInfoDetailVo skuInfoDetailVo = skuInfoDetailVoResult.getData();
        //将数据存储到模型层
        model.addAttribute("categoryView", skuInfoDetailVo.getCategoryView());
        model.addAttribute("skuInfo", skuInfoDetailVo.getSkuInfo());
        model.addAttribute("price", skuInfoDetailVo.getPrice());
        model.addAttribute("spuSaleAttrList", skuInfoDetailVo.getSpuSaleAttrList());
        model.addAttribute("valuesSkuJson", skuInfoDetailVo.getValuesSkuJson());
        return "item/index";
    }

}
