package com.liu.gmall.product.service;

import com.liu.gmall.product.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author L3030
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    BaseAttrInfo attrInfoList(Long category1Id, Long category2Id, Long category3Id);
}