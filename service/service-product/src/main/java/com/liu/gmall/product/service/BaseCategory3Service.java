package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.entity.BaseCategory3;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category3(三级分类表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseCategory3Service extends IService<BaseCategory3> {

    List<BaseCategory3> getByCategory2Id(Long category2Id);
}
