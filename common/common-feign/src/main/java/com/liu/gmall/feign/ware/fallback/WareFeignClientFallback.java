package com.liu.gmall.feign.ware.fallback;
/*
 *@title WareFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 23:16
 */


import com.liu.gmall.feign.ware.WareFeignClient;

public class WareFeignClientFallback implements WareFeignClient {
    @Override
    public String hasStock(Long skuId, Integer num) {
        return "WareFeignClient 远程调用失败，调用降级方法";
    }
}
