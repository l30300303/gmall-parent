package com.liu.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.gmall.product.entity.SkuInfo;

import java.util.List;

/**
* @author L3030
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2023-11-01 16:49:48
* @Entity com.liu.gmall.product.entity.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    SkuInfo findSkuInfoAndImageBySkuId(Long skuId);

    List<Long> findAllSkuIds();
}




