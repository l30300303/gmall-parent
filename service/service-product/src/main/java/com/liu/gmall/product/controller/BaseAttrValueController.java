package com.liu.gmall.product.controller;
/*
 *@title BaseAttrValueController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/4 21:52
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseAttrValue;
import com.liu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseAttrValueController {

    @Autowired
    private BaseAttrValueService baseAttrValueService;


    @GetMapping("/getAttrValueList/{attrId}")
    public Result<List<BaseAttrValue>> getAttrValueList(@PathVariable("attrId") Long attrId){
        List<BaseAttrValue> attrValues = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(attrValues);
    }
}
