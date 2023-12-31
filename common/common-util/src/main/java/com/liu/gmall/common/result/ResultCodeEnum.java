package com.liu.gmall.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),

    PAY_RUN(205, "支付中"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    SECKILL_NO_START(210, "秒杀还没开始"),
    SECKILL_RUN(211, "正在排队中"),
    SECKILL_NO_PAY_ORDER(212, "您有未支付的订单"),
    SECKILL_FINISH(213, "已售罄"),
    SECKILL_END(214, "秒杀已结束"),
    SECKILL_SUCCESS(215, "抢单成功"),
    SECKILL_FAIL(216, "抢单失败"),
    SECKILL_ILLEGAL(217, "请求不合法"),
    SECKILL_ORDER_SUCCESS(218, "下单成功"),
    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),
    ERROR_SPU_REF(222, "被SPU所关联, 不能删除") ,
    ERROR_SKU_REF(223, "被SKU所关联, 不能删除") ,
    SYSTEM_ERROR(224, "网络波动，请稍后重试！"),
    LOGIN_ERROR(225, "用户名或密码错误！"),
    CART_ITEM_COUNT_ERROR(226, "商品数量超过购物车最大限制！"),
    ORDER_FORM_REPEAT(228, "订单重复提交！"),
    SKU_HAS_STOCK_ERROR(229, "订单中存在缺货商品，请返回购物车重新提交订单！"),
    ORDER_INFO_PRICE_ERROR(230, "商品价格异常，请返回购物车重新提交订单！"),
    ORDER_PAY_ERROR(231, "订单超时，请重新提交订单！"),
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
