package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.service.SkuInfoService;
import com.liu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author L3030
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Override
    public Page<SkuInfo> getSkuInfoByPage(Integer pageNo, Integer pageSize) {
        Page<SkuInfo> page = new Page<>(pageNo, pageSize);
        return page(page);
    }

    @Override
    public void onSale(Long skuId) {
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 1);
        update(lambdaUpdateWrapper);
    }

    @Override
    public void cancelSale(Long skuId) {
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 0);
        update(lambdaUpdateWrapper);
    }
}




