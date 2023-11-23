package com.liu.gmall.order.handler.impl;
/*
 *@title PriceValidOrderHandler
 *@description  价格校验
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:29
 */


import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.order.dto.DetailDTO;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.handler.AbstractOrderHandler;
import com.liu.gmall.product.entity.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class PriceValidOrderHandler extends AbstractOrderHandler {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Override
    public int sort() {
        return 3;
    }

    @Override
    public String process(OrderSubmitDto orderSubmitDto, String tradeNo) {
        List<DetailDTO> orderDetailList = orderSubmitDto.getOrderDetailList();
        long count = orderDetailList.stream().filter(detailDTO -> {
            BigDecimal orderPrice = detailDTO.getOrderPrice();
            Result<SkuInfo> infoResult = skuDetailFeignClient.getSkuInfoById(detailDTO.getSkuId());
            SkuInfo skuInfo = infoResult.getData();
            return !orderPrice.equals(skuInfo.getPrice());
        }).count();
        if (count > 0) {
            throw new GmallException(ResultCodeEnum.FAIL);
        }
        AbstractOrderHandler next = getNext();
        if (next != null) {
            return next.process(orderSubmitDto, tradeNo);
        }
        return null;
    }
}
