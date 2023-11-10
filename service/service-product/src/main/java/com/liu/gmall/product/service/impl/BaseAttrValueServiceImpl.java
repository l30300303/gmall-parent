package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.BaseAttrValue;
import com.liu.gmall.product.mapper.BaseAttrValueMapper;
import com.liu.gmall.product.service.BaseAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service实现
* @createDate 2023-11-01 16:49:48
*/
@Service
public class BaseAttrValueServiceImpl extends ServiceImpl<BaseAttrValueMapper, BaseAttrValue>
    implements BaseAttrValueService{

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        LambdaQueryWrapper<BaseAttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BaseAttrValue::getAttrId, attrId);
        return list(lambdaQueryWrapper);
    }
}




