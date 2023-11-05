package com.liu.gmall.product.dto;
/*
 *@title SpuInfoDto
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 15:21
 */


import com.liu.gmall.product.entity.SpuImage;
import com.liu.gmall.product.entity.SpuSaleAttr;
import lombok.Data;

import java.util.List;

@Data
public class SpuInfoDto {

    private Long id;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 商品描述(后台简述）
     */
    private String description;

    /**
     * 三级分类id
     */
    private Long category3Id;

    /**
     * 品牌id
     */
    private Long tmId;

    private List<SpuImage> spuImageList;

    private List<SpuSaleAttr> spuSaleAttrList;
}
