package com.liu.gmall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lfy
 * @Description
 * @create 2022-10-19 11:39
 */
@Data
public class DetailDTO {

    //imgUrl、skuName、orderPrice、skuNum、skuId
    private String imgUrl;
    private String skuName;
    private BigDecimal orderPrice;
    private Integer skuNum;
    private Long skuId;

}
