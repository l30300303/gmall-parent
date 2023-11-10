package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.BaseCategory3;
import com.liu.gmall.product.mapper.BaseCategory3Mapper;
import com.liu.gmall.product.service.BaseCategory3Service;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_category3(三级分类表)】的数据库操作Service实现
* @createDate 2023-11-01 16:49:48
*/
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3>
    implements BaseCategory3Service{

    @Override
    public List<BaseCategory3> getByCategory2Id(Long category2Id) {
        LambdaQueryWrapper<BaseCategory3> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory3::getCategory2Id,category2Id);
        return list(queryWrapper);
    }
}
