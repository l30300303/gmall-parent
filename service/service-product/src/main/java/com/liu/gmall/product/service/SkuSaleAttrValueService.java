package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.entity.SkuSaleAttrValue;
import com.liu.gmall.product.vo.AttrValueConcatVo;

import java.util.List;

/**
* @author L3030
* @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    List<AttrValueConcatVo> findAttrValueConcatBySkuId(Long skuId);
}
