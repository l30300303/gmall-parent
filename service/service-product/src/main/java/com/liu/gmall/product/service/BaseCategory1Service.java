package com.liu.gmall.product.service;

import com.liu.gmall.product.vo.CategoryVo;
import com.liu.gmall.product.entity.BaseCategory1;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category1(一级分类表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseCategory1Service extends IService<BaseCategory1> {

    List<CategoryVo> findAllCategory();
}
