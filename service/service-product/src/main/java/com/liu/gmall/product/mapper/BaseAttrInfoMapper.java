package com.liu.gmall.product.mapper;

import com.liu.gmall.product.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import feign.Param;

import java.util.List;

/**
* @author L3030
* @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
* @createDate 2023-11-01 16:49:48
* @Entity com.liu.gmall.product.entity.BaseAttrInfo
*/
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> attrInfoList(@Param("category1Id") Long category1Id, @Param("category2Id") Long category2Id, @Param("category3Id") Long category3Id);
}




