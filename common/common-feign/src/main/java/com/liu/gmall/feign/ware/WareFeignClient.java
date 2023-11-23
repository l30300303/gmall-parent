package com.liu.gmall.feign.ware;
/*
 *@title WareFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 23:08
 */


import com.liu.gmall.feign.ware.fallback.WareFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ware-manage", url = "http://localhost:9001",fallback = WareFeignClientFallback.class)
public interface WareFeignClient {

    @GetMapping(value = "/hasStock")
    public String hasStock(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num);
}
