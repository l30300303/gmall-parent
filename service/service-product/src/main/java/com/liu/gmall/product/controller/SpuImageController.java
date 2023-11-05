package com.liu.gmall.product.controller;
/*
 *@title SpuImageController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 21:15
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.SpuImage;
import com.liu.gmall.product.service.SpuImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SpuImageController {

    @Autowired
    private SpuImageService spuImageService;

    @GetMapping("/spuImageList/{spuId}")
    public Result<List<SpuImage>> spuImageList(@PathVariable("spuId") Long spuId){
        List<SpuImage> spuImageList = spuImageService.spuImageList(spuId);
        return Result.ok(spuImageList);
    }
}
