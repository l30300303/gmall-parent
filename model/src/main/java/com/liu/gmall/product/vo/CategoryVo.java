package com.liu.gmall.product.vo;
/*
 *@title CategoryView
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 18:46
 */


import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {

    private Long categoryId ;           // 分类的id
    private String categoryName ;       // 分类的名称
    private List<CategoryVo> categoryChild ;      // 存储的是某一个分类所对应子分类数据

}
