package com.liu.gmall.web.controller;
/*
 *@title IndexController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/6 18:36
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.product.BaseCategoryFeignClient;
import com.liu.gmall.product.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BaseCategoryFeignClient baseCategoryFeignClient;

    @GetMapping(value = {"/",""})
    public String index(Model model){
        Result<List<CategoryVo>> allCategory = baseCategoryFeignClient.findAllCategory();
        List<CategoryVo> categoryVoList = allCategory.getData();
        model.addAttribute("list",categoryVoList);
        return "index/index";
    }
}
