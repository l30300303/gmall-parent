package com.liu.gmall.ware.entity;

import lombok.Data;

import java.util.List;

@Data
public class WareStockMsg {         // 封装扣减库存消息

    private Long orderId;
    private String consignee;
    private String consigneeTel;
    private String orderComment;
    private String orderBody;
    private String deliveryAddress;
    private String paymentWay = "2";
    private List<Sku> details;
    
    private Long wareId ; // 拆单的时候需要使用

}