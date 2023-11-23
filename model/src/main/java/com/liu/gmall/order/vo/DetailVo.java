package com.liu.gmall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lfy
 * @Description
 * @create 2022-10-19 11:39
 */
@Data
public class DetailVo {
	
    //imgUrl、skuName、orderPrice、skuNum、skuId
    private String imgUrl ;
    private String skuName;
    private BigDecimal orderPrice;
    private Integer skuNum;
    private Long skuId ;

    //是否有货标志： 1：有货
    private String hasStock = "1";
	
}
