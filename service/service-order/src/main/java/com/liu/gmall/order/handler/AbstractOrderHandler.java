package com.liu.gmall.order.handler;
/*
 *@title AbstractOrderHandler
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:17
 */


import com.liu.gmall.order.dto.OrderSubmitDto;
import lombok.Data;

@Data
public abstract class AbstractOrderHandler {

    private AbstractOrderHandler next;

    public abstract int sort();            // 对步骤进行排序

    public abstract String process(OrderSubmitDto orderSubmitDto, String tradeNo);

}
