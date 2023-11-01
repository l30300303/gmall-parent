package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.BaseCategory2;
import com.liu.gmall.product.service.BaseCategory2Service;
import com.liu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L3030
 * @description 针对表【base_category2(二级分类表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
        implements BaseCategory2Service {


    @Override
    public List<BaseCategory2> getByCategory1Id(Long category1Id) {
        LambdaQueryWrapper<BaseCategory2> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory2::getCategory1Id, category1Id);
        return list(queryWrapper);
    }
}




