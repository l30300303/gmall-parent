package com.liu.gmall.feign.item;
/*
 *@title SkuInfoDetailFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 23:33
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-item")
public interface SkuInfoDetailFeignClient {

    @GetMapping("/api/inner/item/getSkuInfoDetail/{skuId}")
    public Result<SkuInfoDetailVo> getSkuInfoDetail(@PathVariable("skuId") Long skuId);

}
