package com.liu.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.gmall.product.entity.SkuAttrValue;
import com.liu.gmall.search.entity.SearchAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author L3030
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Mapper
* @createDate 2023-11-01 16:49:48
* @Entity com.liu.gmall.product.entity.SkuAttrValue
*/
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {

    List<SearchAttr> findSkuAttrBySkuId(@Param("skuId") Long skuId);
}




