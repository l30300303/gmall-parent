package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.gmall.product.dto.SpuInfoDto;
import com.liu.gmall.product.entity.SpuInfo;

/**
* @author L3030
* @description 针对表【spu_info(商品表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface SpuInfoService extends IService<SpuInfo> {

    Page<SpuInfo> getSpuInfoByPage(Integer pageNo, Integer pageSize, Long category3Id);

    void saveSpuInfo(SpuInfoDto spuInfoDto);
}
