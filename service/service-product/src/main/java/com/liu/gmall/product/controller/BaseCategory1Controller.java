package com.liu.gmall.product.controller;
/*
 *@title BaseCategory1Controller
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 17:01
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseCategory1;
import com.liu.gmall.product.service.BaseCategory1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseCategory1Controller {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getAll(){
        List<BaseCategory1> baseCategory1List = baseCategory1Service.list();
        return Result.ok(baseCategory1List);
    }

}
