package com.liu.gmall.feign.product;
/*
 *@title BaseCategoryFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 18:56
 */

import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.fallback.BaseCategoryFeignClientFallback;
import com.liu.gmall.item.vo.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-product",fallback = BaseCategoryFeignClientFallback.class)
public interface BaseCategoryFeignClient {

    @GetMapping("/api/inner/product/getAllBaseCategory")
    public Result<List<CategoryVo>> findAllCategory();

}
