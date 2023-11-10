package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.dto.SkuInfoDto;
import com.liu.gmall.product.entity.SkuAttrValue;
import com.liu.gmall.product.entity.SkuImage;
import com.liu.gmall.product.entity.SkuInfo;
import com.liu.gmall.product.entity.SkuSaleAttrValue;
import com.liu.gmall.product.mapper.SkuInfoMapper;
import com.liu.gmall.product.service.SkuAttrValueService;
import com.liu.gmall.product.service.SkuImageService;
import com.liu.gmall.product.service.SkuInfoService;
import com.liu.gmall.product.service.SkuSaleAttrValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author L3030
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Page<SkuInfo> getSkuInfoByPage(Integer pageNo, Integer pageSize) {
        Page<SkuInfo> page = new Page<>(pageNo, pageSize);
        return page(page);
    }

    @Override
    public void onSale(Long skuId) {
        //延时双删
        //第一次删除
//        redisTemplate.delete("sku:info:" + skuId);
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 1);
        update(lambdaUpdateWrapper);

        //更新完之后；再次删除
        /*ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.schedule(() -> {
            redisTemplate.delete("sku:info:" + skuId);
        }, 5, TimeUnit.SECONDS);*/
    }

    @Override
    public void cancelSale(Long skuId) {
        LambdaUpdateWrapper<SkuInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SkuInfo::getId, skuId);
        lambdaUpdateWrapper.set(SkuInfo::getIsSale, 0);
        update(lambdaUpdateWrapper);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfoDto skuInfoDto) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoDto, skuInfo);
        skuInfo.setIsSale(0);   //新添加的sku不能直接销售
        save(skuInfo);

        List<SkuImage> skuImageList = skuInfoDto.getSkuImageList();
        skuImageList.stream().forEach(skuImage -> skuImage.setSkuId(skuInfo.getId()));
        skuImageService.saveBatch(skuImageList);

        List<SkuAttrValue> skuAttrValueList = skuInfoDto.getSkuAttrValueList();
        skuAttrValueList.stream().forEach(skuAttrValue -> skuAttrValue.setSkuId(skuInfo.getId()));
        skuAttrValueService.saveBatch(skuAttrValueList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfoDto.getSkuSaleAttrValueList();
        skuSaleAttrValueList.stream().forEach(skuSaleAttrValue -> {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfoDto.getSpuId());
        });
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
    }

    @Override
    public SkuInfo findSkuInfoAndImageBySkuId(Long skuId) {
        return skuInfoMapper.findSkuInfoAndImageBySkuId(skuId);
    }

    @Override
    public List<Long> findAllSkuIds() {
        return skuInfoMapper.findAllSkuIds();
    }
}




