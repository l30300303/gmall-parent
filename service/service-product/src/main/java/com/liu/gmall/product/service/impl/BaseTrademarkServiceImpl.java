package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.product.entity.BaseTrademark;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SpuInfo;
import com.liu.gmall.product.mapper.BaseTrademarkMapper;
import com.liu.gmall.product.mapper.SkuInfoMapper;
import com.liu.gmall.product.mapper.SpuInfoMapper;
import com.liu.gmall.product.service.BaseTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author L3030
 * @description 针对表【base_trademark(品牌表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */


@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark>
        implements BaseTrademarkService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Override
    public Page<BaseTrademark> getBaseTrademarkByPage(Integer pageNum, Integer pageSize) {
        Page<BaseTrademark> page = new Page<>(pageNum, pageSize);
        return page(page);
    }

    @Override
    public void deleteBaseTrademarkById(Long id) {
        LambdaQueryWrapper<SkuInfo> skuInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        skuInfoLambdaQueryWrapper.eq(SkuInfo::getTmId, id);
        List<SkuInfo> list = skuInfoMapper.selectList(skuInfoLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new GmallException(ResultCodeEnum.ERROR_SKU_REF);
        }

        LambdaQueryWrapper<SpuInfo> spuInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        spuInfoLambdaQueryWrapper.eq(SpuInfo::getTmId, id);
        List<SpuInfo> spuInfoList = spuInfoMapper.selectList(spuInfoLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(spuInfoList)){
            throw new GmallException(ResultCodeEnum.ERROR_SPU_REF);
        }

        removeById(id);
    }
}




