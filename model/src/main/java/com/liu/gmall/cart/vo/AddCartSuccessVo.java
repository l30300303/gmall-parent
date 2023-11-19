package com.liu.gmall.cart.vo;
/*
 *@title AddCartSuccessVo
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 21:51
 */


import com.liu.gmall.product.entity.SkuInfo;
import lombok.Data;

@Data
public class AddCartSuccessVo {

    private SkuInfo skuInfo;

    private Integer skuNum;
}
