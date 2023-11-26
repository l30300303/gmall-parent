package com.liu.gmall.order.handler.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
 *@title SaveOrderHandler
 *@description  保存订单
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:31
 */


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.enums.OrderStatus;
import com.liu.gmall.feign.cart.CartFeignClient;
import com.liu.gmall.order.dto.DetailDTO;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.entity.OrderDetail;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.entity.OrderStatusLog;
import com.liu.gmall.order.handler.AbstractOrderHandler;
import com.liu.gmall.order.mapper.OrderInfoMapper;
import com.liu.gmall.order.mapper.OrderStatusLogMapper;
import com.liu.gmall.order.service.OrderDetailService;
import com.liu.gmall.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaveOrderHandler extends AbstractOrderHandler {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public int sort() {
        return 4;
    }

    @Override
    public String process(OrderSubmitDto orderSubmitDto, String tradeNo) {
        //保存订单信息
        OrderInfo orderInfo = saveOrderInfo(orderSubmitDto, tradeNo);
        // 保存明细数据
        saveOrderDetail(orderInfo, orderSubmitDto);
        // 保存订单的状态的日志数据
        saveOrderStatusLog(orderInfo);

        // 删除购物车中选中的购物项数据
        cartFeignClient.deleteCheckedCart();

        // 需要一个延迟任务在30分钟后检查订单状态
        Map<String,String> map = new HashMap<>();
        map.put("orderId",String.valueOf(orderInfo.getId()));
        map.put("userId",String.valueOf(orderInfo.getUserId()));
        rabbitTemplate.convertAndSend("order.exchange","order.info", JSON.toJSONString(map));
        AbstractOrderHandler next = getNext();
        if (next != null) {
            return next.process(orderSubmitDto, tradeNo);
        }
        return String.valueOf(orderInfo.getId());
    }


    private void saveOrderStatusLog(OrderInfo orderInfo) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderInfo.getId());
        orderStatusLog.setOrderStatus(orderInfo.getOrderStatus());
        orderStatusLog.setOperateTime(new Date());
        orderStatusLog.setUserId(orderInfo.getUserId());

        orderStatusLogMapper.insert(orderStatusLog);
    }

    private void saveOrderDetail(OrderInfo orderInfo, OrderSubmitDto orderSubmitDto) {
        List<DetailDTO> orderDetailList = orderSubmitDto.getOrderDetailList();
        List<OrderDetail> orderDetailList1 = orderDetailList.stream().map(detailDTO -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderInfo.getId());
            orderDetail.setSkuId(detailDTO.getSkuId());
            orderDetail.setUserId(orderInfo.getUserId());
            orderDetail.setSkuName(detailDTO.getSkuName());
            orderDetail.setImgUrl(detailDTO.getImgUrl());
            orderDetail.setOrderPrice(detailDTO.getOrderPrice());
            orderDetail.setSkuNum(String.valueOf(detailDTO.getSkuNum()));
            orderDetail.setCreateTime(new Date());
            orderDetail.setSplitTotalAmount(detailDTO.getOrderPrice().multiply(new BigDecimal(detailDTO.getSkuNum())));
            orderDetail.setSplitActivityAmount(new BigDecimal("0"));
            orderDetail.setSplitCouponAmount(new BigDecimal("0"));
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetailList1);
    }

    private OrderInfo saveOrderInfo(OrderSubmitDto orderSubmitDto, String tradeNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setConsignee(orderSubmitDto.getConsignee());
        orderInfo.setConsigneeTel(orderSubmitDto.getConsigneeTel());
        // 获取购物项，计算总价格
        List<DetailDTO> orderDetailList = orderSubmitDto.getOrderDetailList();
        BigDecimal totalAmount = orderDetailList.stream().map(detailDTO -> detailDTO.getOrderPrice().multiply(new BigDecimal(detailDTO.getSkuNum())))
                .reduce(BigDecimal::add).get();

        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();

        //Long.valueOf()返回的数据类型是包装类Long  Long.parseLong()返回的是基本数据类型long 直接返回对应类型的数据避免自动装箱，开箱
        orderInfo.setUserId(Long.valueOf(userAuthInfo.getUserId()));
        orderInfo.setPaymentWay(orderSubmitDto.getPaymentWay());
        orderInfo.setDeliveryAddress(orderSubmitDto.getDeliveryAddress());
        orderInfo.setOrderComment(orderSubmitDto.getOrderComment());
        orderInfo.setOutTradeNo(tradeNo);

        DetailDTO detailDTO = orderDetailList.get(0);
        orderInfo.setTradeBody(detailDTO.getSkuName());
        orderInfo.setCreateTime(new Date());
        long expireTime = LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        orderInfo.setExpireTime(new Date(expireTime));

        orderInfo.setProcessStatus(OrderStatus.UNPAID.name());
        orderInfo.setTrackingNo("");
        orderInfo.setParentOrderId(0L);

        orderInfo.setImgUrl(detailDTO.getImgUrl());
        orderInfo.setProvinceId(0L);
        orderInfo.setOperateTime(new Date());

        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
        orderInfo.setFeightFee(new BigDecimal("0"));
//        orderInfo.setRefundableTime(new Date());
        orderInfoService.save(orderInfo);
        return orderInfo;
    }
}
