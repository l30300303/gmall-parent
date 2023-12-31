package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.entity.SpuSaleAttr;

import java.util.List;

/**
* @author L3030
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> spuSaleAttrList(Long spuId);

    List<SpuSaleAttr> findSpuSaleAttrAndSaleAttrValue(Long skuId);
}
