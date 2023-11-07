package com.liu.gmall.item.api;
/*
 *@title SkuInfoDetailApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 23:37
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.item.SkuInfoDetailFeignClient;
import com.liu.gmall.item.service.SkuInfoDetailService;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/inner/item")
public class SkuInfoDetailApiController {

    @Autowired
    private SkuInfoDetailService skuInfoDetailService;

    @GetMapping("/getSkuInfoDetail/{skuId}")
    public Result<SkuInfoDetailVo> getSkuInfoDetail(@PathVariable("skuId") Long skuId) {
        SkuInfoDetailVo skuInfoDetail = skuInfoDetailService.getSkuInfoDetail(skuId);
        return Result.ok(skuInfoDetail);
    }

}
