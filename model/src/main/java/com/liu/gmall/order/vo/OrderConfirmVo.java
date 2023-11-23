package com.liu.gmall.order.vo;

import com.liu.gmall.user.entity.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lfy
 * @Description
 * @create 2022-10-19 11:37
 */
@Data
public class OrderConfirmVo {

    //商品列表（）
    private List<DetailVo> detailArrayList;

    //商品总数
    private Integer totalNum;

    //商品总额
    private BigDecimal totalAmount;

    //用户收货地址列表
    private List<UserAddress> userAddressList;

    //交易号
    private String tradeNo;

}
