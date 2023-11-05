package com.liu.gmall.product.controller;
/*
 *@title SpuSaleAttrController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 21:19
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SpuSaleAttrController {

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> spuSaleAttrList(@PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.spuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
}
