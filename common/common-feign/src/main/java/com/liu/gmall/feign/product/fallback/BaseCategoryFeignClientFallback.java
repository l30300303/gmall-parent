package com.liu.gmall.feign.product.fallback;
/*
 *@title BaseCategoryFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 20:28
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.BaseCategoryFeignClient;
import com.liu.gmall.item.vo.CategoryVo;

import java.util.List;

public class BaseCategoryFeignClientFallback implements BaseCategoryFeignClient {

    @Override
    public Result<List<CategoryVo>> findAllCategory() {
        return Result.ok();
    }

}
