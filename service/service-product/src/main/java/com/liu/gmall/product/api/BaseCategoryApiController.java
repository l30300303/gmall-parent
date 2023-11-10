package com.liu.gmall.product.api;

/*
 *@title BaseCategoryApiController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 18:58
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.product.service.BaseCategory1Service;
import com.liu.gmall.product.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/product")
public class BaseCategoryApiController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @GetMapping("/getAllBaseCategory")
    public Result<List<CategoryVo>> findAllCategory() {

        List<CategoryVo> categoryVoList = baseCategory1Service.findAllCategory();
        return Result.ok(categoryVoList);
    }
}
