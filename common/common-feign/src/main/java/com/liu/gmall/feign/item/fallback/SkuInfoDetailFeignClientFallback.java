package com.liu.gmall.feign.item.fallback;
/*
 *@title SkuInfoDetailFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 23:34
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.item.SkuInfoDetailFeignClient;
import com.liu.gmall.item.vo.SkuInfoDetailVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkuInfoDetailFeignClientFallback implements SkuInfoDetailFeignClient {
    @Override
    public Result<SkuInfoDetailVo> getSkuInfoDetail(Long skuId) {
        log.info("SkuInfoDetailFeignClientFallback。。。。。getSkuInfoDetail........");
        return Result.ok();
    }
}
