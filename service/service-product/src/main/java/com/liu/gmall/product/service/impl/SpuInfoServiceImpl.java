package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.dto.SpuInfoDto;
import com.liu.gmall.product.entity.SpuImage;
import com.liu.gmall.product.entity.SpuInfo;
import com.liu.gmall.product.entity.SpuSaleAttr;
import com.liu.gmall.product.entity.SpuSaleAttrValue;
import com.liu.gmall.product.mapper.SpuInfoMapper;
import com.liu.gmall.product.service.SpuImageService;
import com.liu.gmall.product.service.SpuInfoService;
import com.liu.gmall.product.service.SpuSaleAttrService;
import com.liu.gmall.product.service.SpuSaleAttrValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author L3030
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2023-11-01 16:49:48
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    private SpuImageService spuImageService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    public Page<SpuInfo> getSpuInfoByPage(Integer pageNo, Integer pageSize, Long category3Id) {
        Page<SpuInfo> page = new Page<>(pageNo,pageSize);
        LambdaQueryWrapper<SpuInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SpuInfo::getCategory3Id,category3Id);
        return page(page,lambdaQueryWrapper);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfoDto spuInfoDto) {
        SpuInfo spuInfo = new SpuInfo();
        BeanUtils.copyProperties(spuInfoDto,spuInfo);
        save(spuInfo);
        List<SpuImage> spuImages = spuInfoDto.getSpuImageList();
        spuImages.forEach(spuImage -> spuImage.setSpuId(spuInfo.getId()));
        spuImageService.saveBatch(spuImages);
        //spu销售属性
        List<SpuSaleAttr> spuSaleAttrs = spuInfoDto.getSpuSaleAttrList();
        //spu销售属性值
        List<SpuSaleAttrValue> spuSaleAttrValues = new ArrayList<>();
        spuSaleAttrs.stream().forEach(spuSaleAttr -> {
            spuSaleAttr.getSpuSaleAttrValueList().forEach(spuSaleAttrValue -> {
                spuSaleAttrValue.setSpuId(spuInfo.getId());
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            });
            spuSaleAttr.setSpuId(spuInfo.getId());
            spuSaleAttrValues.addAll(spuSaleAttr.getSpuSaleAttrValueList());
        });
       spuSaleAttrService.saveBatch(spuSaleAttrs);
       spuSaleAttrValueService.saveBatch(spuSaleAttrValues);
    }
}




