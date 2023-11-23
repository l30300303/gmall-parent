package com.liu.gmall.order.handler.impl;
/*
 *@title WareValidOrderHandler
 *@description  库存校验
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:30
 */


import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.feign.ware.WareFeignClient;
import com.liu.gmall.order.dto.DetailDTO;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.handler.AbstractOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WareValidOrderHandler extends AbstractOrderHandler {

    @Autowired
    private WareFeignClient wareFeignClient;

    @Override
    public int sort() {
        return 2;
    }

    @Override
    public String process(OrderSubmitDto orderSubmitDto, String tradeNo) {
        //获取明细数据
        List<DetailDTO> orderDetailList = orderSubmitDto.getOrderDetailList();
        long count = orderDetailList.stream().filter(detailDTO -> {
            Long skuId = detailDTO.getSkuId();
            Integer skuNum = detailDTO.getSkuNum();
            String hasStock = wareFeignClient.hasStock(skuId, skuNum);
            return "0".equals(hasStock);
        }).count();
        if (count > 0) {
            throw new GmallException(ResultCodeEnum.SKU_HAS_STOCK_ERROR);
        }
        AbstractOrderHandler next = getNext();
        if (next != null) {
            return next.process(orderSubmitDto,tradeNo);
        }
        return null;
    }
}
