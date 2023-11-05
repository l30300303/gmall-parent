package com.liu.gmall.product.controller;
/*
 *@title SpuInfoController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/5 12:56
 */


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.SpuInfo;
import com.liu.gmall.product.dto.SpuInfoDto;
import com.liu.gmall.product.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("/{page}/{limit}")
    public Result<Page<SpuInfo>> getSpuInfoByPage(@PathVariable("page") Integer pageNo,
                                                  @PathVariable("limit") Integer pageSize,
                                                  @RequestParam("category3Id") Long category3Id) {
        Page<SpuInfo> spuInfoList = spuInfoService.getSpuInfoByPage(pageNo, pageSize, category3Id);
        return Result.ok(spuInfoList);
    }

    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfoDto spuInfoDto){
        spuInfoService.saveSpuInfo(spuInfoDto);
        return Result.ok();
    }

}
