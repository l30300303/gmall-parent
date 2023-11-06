package com.liu.gmall.product.mapper;

import com.liu.gmall.item.vo.CategoryVo;
import com.liu.gmall.product.entity.BaseCategory1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category1(一级分类表)】的数据库操作Mapper
* @createDate 2023-11-01 16:49:48
* @Entity com.liu.gmall.product.entity.BaseCategory1
*/
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {

    List<CategoryVo> findAllCategory();
}




