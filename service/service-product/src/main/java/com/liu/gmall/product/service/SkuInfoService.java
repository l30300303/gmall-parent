package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.product.entity.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author L3030
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface SkuInfoService extends IService<SkuInfo> {

    Page<SkuInfo> getSkuInfoByPage(Integer pageNo, Integer pageSize);

    void onSale(Long skuId);

    void cancelSale(Long skuId);
}
