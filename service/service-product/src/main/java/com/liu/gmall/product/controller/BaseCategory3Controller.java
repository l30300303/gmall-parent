package com.liu.gmall.product.controller;
/*
 *@title BaseCategory3Controller
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 19:22
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseCategory3;
import com.liu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseCategory3Controller {

    @Autowired
    private BaseCategory3Service baseCategory3Service;

    @GetMapping("/getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getByCategory2Id(@PathVariable("category2Id") Long category2Id) {
        List<BaseCategory3> baseCategory3List = baseCategory3Service.getByCategory2Id(category2Id);
        return Result.ok(baseCategory3List);
    }
}
