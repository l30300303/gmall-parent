package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.entity.BaseAttrValue;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    List<BaseAttrValue> getAttrValueList(Long attrId);
}
