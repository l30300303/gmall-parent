package com.liu.gmall.product.controller;
/*
 *@title ResetBloomFilterController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/12 19:40
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.service.ResetBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class ResetBloomFilterController {

    @Autowired
    private ResetBloomFilter resetBloomFilter;

    @GetMapping("/resetBloomFilter")
    public Result<String> resetBloomFilter(){
        resetBloomFilter.resetBloomFilter();
        return Result.ok("布隆过滤器重置成功！！");
    }
}
