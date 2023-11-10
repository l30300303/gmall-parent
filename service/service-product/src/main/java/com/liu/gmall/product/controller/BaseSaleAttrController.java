package com.liu.gmall.product.controller;
/*
 *@title BaseSaleAttrController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 14:54
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseSaleAttr;
import com.liu.gmall.product.service.BaseSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseSaleAttrController {

    @Autowired
    private BaseSaleAttrService baseSaleAttrService;

    @GetMapping("/baseSaleAttrList")
    public Result<List<BaseSaleAttr>> getBaseSaleAttrALl(){
        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);
    }
}
