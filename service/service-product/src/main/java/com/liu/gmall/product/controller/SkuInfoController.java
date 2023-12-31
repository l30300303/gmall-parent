package com.liu.gmall.product.controller;
/*
 *@title SkuInfoController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 16:50
 */


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.dto.SkuInfoDto;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/list/{page}/{limit}")
    public Result<Page<SkuInfo>> getSkuInfoByPage(@PathVariable("page") Integer pageNo, @PathVariable("limit") Integer pageSize) {
        Page<SkuInfo> page = skuInfoService.getSkuInfoByPage(pageNo, pageSize);
        return Result.ok(page);
    }

    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSale(skuId);
        return Result.ok();
    }

    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }

    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfoDto skuInfoDto) {
        skuInfoService.saveSkuInfo(skuInfoDto);
        return Result.ok();
    }
}
