package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.SpuImage;
import com.liu.gmall.product.service.SpuImageService;
import com.liu.gmall.product.mapper.SpuImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L3030
 * @description 针对表【spu_image(商品图片表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class SpuImageServiceImpl extends ServiceImpl<SpuImageMapper, SpuImage>
        implements SpuImageService {

    @Override
    public List<SpuImage> spuImageList(Long spuId) {
        LambdaQueryWrapper<SpuImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SpuImage::getSpuId, spuId);
        List<SpuImage> list = list(lambdaQueryWrapper);
        return list;
    }
}




