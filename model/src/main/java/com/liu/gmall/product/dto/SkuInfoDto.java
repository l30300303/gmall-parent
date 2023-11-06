package com.liu.gmall.product.dto;
/*
 *@title SkuInfoDto
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 9:17
 */


import com.liu.gmall.product.entity.SkuAttrValue;
import com.liu.gmall.product.entity.SkuImage;
import com.liu.gmall.product.entity.SkuSaleAttrValue;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfoDto {

    private Long id;

    private Long spuId;

    private BigDecimal price;

    private String skuName;

    private BigDecimal weight;

    private String skuDesc;

    private Long category3Id;

    private String skuDefaultImg;

    private Long tmId;

    private List<SkuImage> skuImageList;    //sku图片数据

    private List<SkuAttrValue> skuAttrValueList;    //sku平台属性

    private List<SkuSaleAttrValue> skuSaleAttrValueList;    //sku平台属性值
}
