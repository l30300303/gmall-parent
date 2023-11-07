package com.liu.gmall.item.vo;
/*
 *@title SkuInfoDetailVo
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 21:14
 */


import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfoDetailVo {

    private CategoryView categoryView;
    private SkuInfo skuInfo;
    private BigDecimal price;

    private List<SpuSaleAttr> spuSaleAttrList;
    private String valuesSkuJson;
}
