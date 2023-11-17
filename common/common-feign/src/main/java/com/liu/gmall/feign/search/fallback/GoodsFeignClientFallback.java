package com.liu.gmall.feign.search.fallback;
/*
 *@title GoodsFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 19:34
 */

import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.search.GoodsFeignClient;
import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.vo.SearchResponseVo;

public class GoodsFeignClientFallback implements GoodsFeignClient {

    @Override
    public Result saveGoods(Goods goods) {
        return Result.ok();
    }

    @Override
    public Result deleteById(Long skuId) {
        return Result.ok();
    }

    @Override
    public Result<SearchResponseVo> search(SearchParamDto searchParamDto) {

        return Result.ok();
    }
}
