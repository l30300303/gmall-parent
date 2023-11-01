package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.BaseAttrInfo;
import com.liu.gmall.product.service.BaseAttrInfoService;
import com.liu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author L3030
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {

    @Override
    public BaseAttrInfo attrInfoList(Long category1Id, Long category2Id, Long category3Id) {

        return null;
    }
}




