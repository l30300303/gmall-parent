package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.entity.BaseAttrInfo;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

}
