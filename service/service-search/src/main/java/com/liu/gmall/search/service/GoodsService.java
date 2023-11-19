package com.liu.gmall.search.service;
/*
 *@title GoodsService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/15 18:52
 */


import com.liu.gmall.search.dto.SearchParamDto;
import com.liu.gmall.search.entity.Goods;
import com.liu.gmall.search.vo.SearchResponseVo;

public interface GoodsService {
    void saveGoods(Goods goods);

    void deleteById(Long skuId);

    SearchResponseVo search(SearchParamDto searchParamDto);
}
