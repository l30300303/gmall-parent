package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.vo.CategoryVo;
import com.liu.gmall.product.entity.BaseCategory1;
import com.liu.gmall.product.service.BaseCategory1Service;
import com.liu.gmall.product.mapper.BaseCategory1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category1(一级分类表)】的数据库操作Service实现
* @createDate 2023-11-01 16:49:48
*/
@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
    implements BaseCategory1Service{

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Override
    public List<CategoryVo> findAllCategory() {
        return baseCategory1Mapper.findAllCategory();
    }
}




