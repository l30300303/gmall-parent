package com.liu.gmall.product.mapper;

import com.liu.gmall.product.entity.SkuSaleAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author L3030
* @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Mapper
* @createDate 2023-11-01 16:49:48
* @Entity com.liu.gmall.product.entity.SkuSaleAttrValue
*/
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {

    List<AttrValueConcatVo> findAttrValueConcatBySkuId(@Param("skuId") Long skuId);
}




