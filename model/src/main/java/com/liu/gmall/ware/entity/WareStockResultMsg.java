package com.liu.gmall.ware.entity;

import lombok.Data;

@Data
public class WareStockResultMsg {

    private Long orderId; //订单
    private String status; //扣减状态

}