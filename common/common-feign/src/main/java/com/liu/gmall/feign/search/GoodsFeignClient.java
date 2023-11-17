package com.liu.gmall.feign.search;
/*
 *@title GoodsFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 19:33
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.search.fallback.GoodsFeignClientFallback;
import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.vo.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-search", fallback = GoodsFeignClientFallback.class)
public interface GoodsFeignClient {

    @PostMapping("/api/inner/search/saveGoods")
    public Result saveGoods(@RequestBody Goods goods);

    @DeleteMapping("/api/inner/search/deleteById/{skuId}")
    public Result deleteById(@PathVariable("skuId")Long skuId);

    @PostMapping("/api/inner/search/search")
    public Result<SearchResponseVo> search(@RequestBody SearchParamDto searchParamDto);

}
