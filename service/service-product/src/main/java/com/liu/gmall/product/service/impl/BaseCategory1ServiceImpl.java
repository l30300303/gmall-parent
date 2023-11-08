package com.liu.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.item.vo.CategoryView;
import com.liu.gmall.product.vo.CategoryVo;
import com.liu.gmall.product.entity.BaseCategory1;
import com.liu.gmall.product.service.BaseCategory1Service;
import com.liu.gmall.product.mapper.BaseCategory1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author L3030
 * @description 针对表【base_category1(一级分类表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
        implements BaseCategory1Service {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<CategoryVo> findAllCategory() {
        String result = redisTemplate.opsForValue().get("gmall:category");
        if (!StringUtils.isEmpty(result)) {
            return JSON.parseArray(result, CategoryVo.class);
        }
        List<CategoryVo> categoryVoList = baseCategory1Mapper.findAllCategory();
        redisTemplate.opsForValue().set("gmall:category", JSON.toJSONString(categoryVoList));
        return categoryVoList;
    }

    @Override
    public CategoryView findCategoryViewBySkuId(Long skuId) {
        return baseCategory1Mapper.findCategoryViewBySkuId(skuId);
    }
}




