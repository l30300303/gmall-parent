package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.SkuSaleAttrValue;
import com.liu.gmall.product.mapper.SkuSaleAttrValueMapper;
import com.liu.gmall.product.service.SkuSaleAttrValueService;
import com.liu.gmall.product.vo.AttrValueConcatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author L3030
* @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Service实现
* @createDate 2023-11-01 16:49:48
*/
@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue>
    implements SkuSaleAttrValueService{

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Override
    public List<AttrValueConcatVo> findAttrValueConcatBySkuId(Long skuId) {
        return skuSaleAttrValueMapper.findAttrValueConcatBySkuId(skuId);
    }
}




