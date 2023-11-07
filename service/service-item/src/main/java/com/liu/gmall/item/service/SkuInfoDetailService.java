package com.liu.gmall.item.service;
/*
 *@title SkuInfoDetailService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/7 18:34
 */


import com.liu.gmall.item.vo.SkuInfoDetailVo;

public interface SkuInfoDetailService {

    SkuInfoDetailVo getSkuInfoDetail(Long skuId);
}
