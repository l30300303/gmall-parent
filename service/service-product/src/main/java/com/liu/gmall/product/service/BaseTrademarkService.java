package com.liu.gmall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.product.entity.BaseTrademark;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author L3030
* @description 针对表【base_trademark(品牌表)】的数据库操作Service
* @createDate 2023-11-01 16:49:48
*/
public interface BaseTrademarkService extends IService<BaseTrademark> {

    Page<BaseTrademark> getBaseTrademarkByPage(Integer pageNum, Integer pageSize);

    void deleteBaseTrademarkById(Long id);
}
