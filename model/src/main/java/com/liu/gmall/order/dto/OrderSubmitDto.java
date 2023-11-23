package com.liu.gmall.order.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class OrderSubmitDto {

    // 收货人
    @NotBlank(message = "收件人不能为空")
    private String consignee;

    // 收货人电话号码
    @NotBlank(message = "收件人电话号码不能为空")
    @Length(min = 5,max = 11,message = "收件人电话号码格式不正确")
    private String consigneeTel;

    // 收货人地址
    @NotBlank(message = "收货人地址不能为空")
    private String deliveryAddress;

    // 支付方式
    private String paymentWay = "online";

    // 订单备注
    private String orderComment;

    // 订单明细
    private List<DetailDTO> orderDetailList;

}
