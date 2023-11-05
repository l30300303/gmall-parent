package com.liu.gmall.product.service;

import com.liu.gmall.product.entity.SpuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author L3030
* @description 针对表【spu_image(商品图片表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface SpuImageService extends IService<SpuImage> {

    List<SpuImage> spuImageList(Long spuId);
}
