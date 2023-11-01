package com.liu.gmall.product.service;

import com.liu.gmall.product.entity.BaseCategory2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category2(二级分类表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseCategory2Service extends IService<BaseCategory2> {

    List<BaseCategory2> getByCategory1Id(Long category1Id);
}
