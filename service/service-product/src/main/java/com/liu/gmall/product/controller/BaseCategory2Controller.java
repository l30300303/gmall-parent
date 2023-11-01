package com.liu.gmall.product.controller;
/*
 *@title BaseCategory2Controller
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 18:42
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseCategory2;
import com.liu.gmall.product.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseCategory2Controller {

    @Autowired
    private BaseCategory2Service baseCategory2Service;


    @GetMapping("/getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getByCategory1Id(@PathVariable("category1Id") Long category1Id) {
        List<BaseCategory2> baseCategory2List = baseCategory2Service.getByCategory1Id(category1Id);
        return Result.ok(baseCategory2List);
    }
}
