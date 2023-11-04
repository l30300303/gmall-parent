package com.liu.gmall.product.controller;
/*
 *@title BaseAttrInfoController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/1 19:32
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseAttrInfo;
import com.liu.gmall.product.entity.BaseAttrValue;
import com.liu.gmall.product.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zipkin2.Call;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseAttrInfoController {

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id") Long category1Id,
                                                   @PathVariable("category2Id") Long category2Id,
                                                   @PathVariable("category3Id") Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoService.attrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }

    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }
}
