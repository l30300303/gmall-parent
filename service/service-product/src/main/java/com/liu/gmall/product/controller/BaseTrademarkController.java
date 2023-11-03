package com.liu.gmall.product.controller;
/*
 *@title BaseTrademarkController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/3 16:27
 */


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.entity.BaseTrademark;
import com.liu.gmall.product.service.BaseTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zipkin2.Call;

@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;


    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result<Page<BaseTrademark>> getBaseTrademarkByPage(@PathVariable("page") Integer pageNum, @PathVariable("limit") Integer pageSize) {
        Page<BaseTrademark> page = baseTrademarkService.getBaseTrademarkByPage(pageNum, pageSize);
        return Result.ok(page);
    }

    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result deleteBaseTrademarkById(@PathVariable("id") Long id) {
        baseTrademarkService.deleteBaseTrademarkById(id);
        return Result.ok();
    }

    @GetMapping("/baseTrademark/get/{id}")
    public Result<BaseTrademark> getBaseTrademarkById(@PathVariable("id") Long id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

}
