package com.liu.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.product.entity.BaseAttrInfo;
import com.liu.gmall.product.entity.BaseAttrValue;
import com.liu.gmall.product.mapper.BaseAttrInfoMapper;
import com.liu.gmall.product.service.BaseAttrInfoService;
import com.liu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author L3030
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
 * @createDate 2023-11-01 16:49:48
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueService baseAttrValueService;


    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.attrInfoList(category1Id, category2Id, category3Id);
        return baseAttrInfoList;
    }

    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() != null) {
            updateById(baseAttrInfo);
            LambdaQueryWrapper<BaseAttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(BaseAttrValue::getAttrId, baseAttrInfo.getId());
            baseAttrValueService.remove(lambdaQueryWrapper);
        } else {
            save(baseAttrInfo);
        }

        List<BaseAttrValue> baseAttrValueList = baseAttrInfo.getAttrValueList();
        baseAttrValueList.stream().forEach(baseAttrValue -> baseAttrValue.setAttrId(baseAttrInfo.getId()));
        baseAttrValueService.saveBatch(baseAttrValueList);
    }

}




