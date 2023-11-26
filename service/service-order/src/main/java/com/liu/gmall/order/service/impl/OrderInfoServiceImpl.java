package com.liu.gmall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.enums.OrderStatus;
import com.liu.gmall.enums.PaymentStatus;
import com.liu.gmall.enums.ProcessStatus;
import com.liu.gmall.feign.cart.CartFeignClient;
import com.liu.gmall.feign.user.UserAddressFeignClient;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.entity.OrderDetail;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.entity.OrderStatusLog;
import com.liu.gmall.order.entity.PaymentInfo;
import com.liu.gmall.order.handler.manager.OrderHandlerManager;
import com.liu.gmall.order.mapper.OrderDetailMapper;
import com.liu.gmall.order.mapper.OrderInfoMapper;
import com.liu.gmall.order.mapper.OrderStatusLogMapper;
import com.liu.gmall.order.mapper.PaymentInfoMapper;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.order.vo.DetailVo;
import com.liu.gmall.order.vo.OrderConfirmVo;
import com.liu.gmall.user.entity.UserAddress;
import com.liu.gmall.ware.entity.Sku;
import com.liu.gmall.ware.entity.WareStockMsg;
import com.liu.gmall.ware.entity.WareStockResultMsg;
import com.liu.gmall.ware.vo.WareSkuMapVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author L3030
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
 * @createDate 2023-11-20 21:29:23
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private UserAddressFeignClient userAddressFeignClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderHandlerManager orderHandlerManager;

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public OrderConfirmVo trade() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> cf1 = CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            Result<List<CartItem>> itemResult = cartFeignClient.findByUserId();
            List<CartItem> cartItemList = itemResult.getData();
            List<DetailVo> detailVoList = cartItemList.stream().map(cartItem -> {
                DetailVo detailVo = new DetailVo();
                detailVo.setImgUrl(cartItem.getImgUrl());
                detailVo.setSkuName(cartItem.getSkuName());
                detailVo.setOrderPrice(cartItem.getSkuPrice());
                detailVo.setSkuNum(cartItem.getSkuNum());
                detailVo.setSkuId(cartItem.getSkuId());
                detailVo.setHasStock("1");
                return detailVo;
            }).collect(Collectors.toList());
            orderConfirmVo.setDetailArrayList(detailVoList);
            return detailVoList;
        }, threadPoolExecutor).thenAcceptAsync(detailVos -> {
            Integer totalNum = detailVos.stream().map(DetailVo::getSkuNum).reduce(Integer::sum).get();
            orderConfirmVo.setTotalNum(totalNum);
            BigDecimal totalAmount = detailVos.stream().map(detailVo -> detailVo.getOrderPrice().multiply(new BigDecimal(detailVo.getSkuNum())))
                    .reduce(BigDecimal::add).get();
            orderConfirmVo.setTotalAmount(totalAmount);
            // 生成交易号，并且把交易号进行返回
            String tradeNo = UUID.randomUUID().toString().replace("-", "");
            orderConfirmVo.setTradeNo(tradeNo);
            // 把交易号保存到Redis中
            redisTemplate.opsForValue().set("order:info:tradeno:" + tradeNo, "1", 5, TimeUnit.MINUTES);
            orderConfirmVo.setTradeNo(tradeNo);
        });

        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            Result<List<UserAddress>> addressResult = userAddressFeignClient.findByUserId();
            List<UserAddress> userAddressList = addressResult.getData();
            orderConfirmVo.setUserAddressList(userAddressList);
        }, threadPoolExecutor);

        CompletableFuture.allOf(cf1, cf2).join();
        return orderConfirmVo;
    }

    @Override
    public String submitOrder(String tradeNo, OrderSubmitDto orderSubmitDTO) {
        return orderHandlerManager.exec(orderSubmitDTO, tradeNo);
    }

    @Override
    public Page<OrderInfo> getOrder(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        lambdaQueryWrapper.eq(OrderInfo::getUserId, Long.valueOf(userAuthInfo.getUserId()));
        Page<OrderInfo> page = new Page<>(pageNum, pageSize);
        return page(page, lambdaQueryWrapper);
    }

    //关闭订单
    @Override
    public void closeOrder(Long orderId, Long userId) {
        LambdaUpdateWrapper<OrderInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(OrderInfo::getId, orderId);
        lambdaUpdateWrapper.eq(OrderInfo::getUserId, userId);
        lambdaUpdateWrapper.eq(OrderInfo::getOrderStatus, OrderStatus.UNPAID.name());
        lambdaUpdateWrapper.eq(OrderInfo::getProcessStatus, ProcessStatus.UNPAID.name());
        lambdaUpdateWrapper.set(OrderInfo::getOrderStatus, OrderStatus.CLOSED.name());
        lambdaUpdateWrapper.set(OrderInfo::getProcessStatus, ProcessStatus.CLOSED.name());
        boolean update = update(lambdaUpdateWrapper);
        //保存订单状态数据
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        if (update) {
            orderStatusLog.setOrderStatus(OrderStatus.CLOSED.name());
        } else {
            String status = getOrderStatusById(String.valueOf(orderId), userId);
            orderStatusLog.setOrderStatus(status);
        }
        orderStatusLog.setOperateTime(new Date());
        orderStatusLog.setUserId(userId);
        orderStatusLogMapper.insert(orderStatusLog);
    }

    @Override
    public OrderInfo findOrderInfoById(Long orderId) {
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        String userId = userAuthInfo.getUserId();
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderInfo::getId, orderId);
        lambdaQueryWrapper.eq(OrderInfo::getUserId, Long.parseLong(userId));
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public void orderPayedUpdateOrderStatus(Map<String, String> map) {
        //保存支付信息
        PaymentInfo paymentInfo = savePaymentInfo(map);

        //修改订单状态
        OrderInfo orderInfo = updateOrderStatusPayed(paymentInfo);

        //保存订单日志信息
        saveOrderStatusLog(orderInfo);

        //库减库存
        WareStockMsg stockMsg = buildWareStockMsg(orderInfo);
        rabbitTemplate.convertAndSend("exchange.direct.ware.stock", "ware.stock", JSON.toJSONString(stockMsg));


    }

    @Override
    public void skuWareStockUpdateStatus(String msg) {
        //解析msg
        WareStockResultMsg wareStockResultMsg = JSON.parseObject(msg, WareStockResultMsg.class);
        //修改订单状态
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>() ;
        lambdaQueryWrapper.eq(OrderInfo::getId , wareStockResultMsg.getOrderId()) ;
        OrderInfo orderInfo = this.getOne(lambdaQueryWrapper);
        LambdaUpdateWrapper<OrderInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(OrderInfo::getId,wareStockResultMsg.getOrderId());
        String status = wareStockResultMsg.getStatus();
        // 修改订单状态
        OrderStatus orderStatus = null ;
        ProcessStatus processStatus = null ;
        switch (status){
            case "DEDUCTED":
                orderStatus = OrderStatus.WAITING_DELEVER ;
                processStatus = ProcessStatus.NOTIFIED_WARE ;
                lambdaUpdateWrapper.set(OrderInfo::getOrderStatus,OrderStatus.WAITING_DELEVER.name());
                lambdaUpdateWrapper.set(OrderInfo::getProcessStatus,ProcessStatus.WAITING_DELEVER.name());
                break;
            case "OUT_OF_STOCK":
                orderStatus = OrderStatus.WAITING_SCHEDULE ;
                processStatus = ProcessStatus.STOCK_EXCEPTION ;
                lambdaUpdateWrapper.set(OrderInfo::getOrderStatus,OrderStatus.WAITING_SCHEDULE.name());
                lambdaUpdateWrapper.set(OrderInfo::getProcessStatus,ProcessStatus.STOCK_EXCEPTION.name());
                break;
        }
        lambdaUpdateWrapper.eq(OrderInfo::getOrderStatus,OrderStatus.PAID.name());
        lambdaUpdateWrapper.eq(OrderInfo::getProcessStatus,ProcessStatus.PAID.name());
        lambdaUpdateWrapper.eq(OrderInfo::getUserId,orderInfo.getUserId());
        this.update(lambdaUpdateWrapper);
        orderInfo.setOrderStatus(orderStatus.name());
        orderInfo.setProcessStatus(processStatus.name());
        saveOrderStatusLog(orderInfo);
    }

    @Override
    public List<WareStockMsg> orderSplit(Long orderId, String wareSkuMap) {
        List<WareSkuMapVo> wareSkuMapVos = JSON.parseArray(wareSkuMap, WareSkuMapVo.class);
        OrderInfo oldOrderInfo = getById(orderId);
        // 遍历list集合，list集合中的每一个元素就是一个新的订单
        List<WareStockMsg> wareStockMsgList = new ArrayList<>() ;
        for(WareSkuMapVo wareSkuMapVo : wareSkuMapVos) {

            // 创建新的订单对象，封装要保存的订单数据
            OrderInfo orderInfo = new OrderInfo() ;
            orderInfo.setConsignee(oldOrderInfo.getConsignee());
            orderInfo.setConsigneeTel(oldOrderInfo.getConsigneeTel());

            // 获取新订单的明细数据
            List<Long> skuIds = wareSkuMapVo.getSkuIds();
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>() ;
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId , orderId) ;
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getUserId , oldOrderInfo.getUserId()) ;
            orderDetailLambdaQueryWrapper.in(OrderDetail::getSkuId , skuIds) ;
            List<OrderDetail> orderDetails = orderDetailMapper.selectList(orderDetailLambdaQueryWrapper);
            BigDecimal totalAmount = orderDetails.stream().map(orderDetail -> {
                return orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum())) ;
            }).reduce((o1, o2) -> o1.add(o2)).get();
            orderInfo.setTotalAmount(totalAmount);

            orderInfo.setOrderStatus(oldOrderInfo.getOrderStatus());
            orderInfo.setUserId(oldOrderInfo.getUserId());
            orderInfo.setPaymentWay(oldOrderInfo.getPaymentWay());
            orderInfo.setDeliveryAddress(oldOrderInfo.getDeliveryAddress());
            orderInfo.setOrderComment(oldOrderInfo.getOrderComment());
            orderInfo.setOutTradeNo(oldOrderInfo.getOutTradeNo());
            OrderDetail orderDetail = orderDetails.get(0);
            orderInfo.setTradeBody(orderDetail.getSkuName());
            orderInfo.setCreateTime(new Date());
            orderInfo.setExpireTime(oldOrderInfo.getExpireTime());
            orderInfo.setProcessStatus(oldOrderInfo.getProcessStatus());
            orderInfo.setParentOrderId(oldOrderInfo.getId());
            orderInfo.setImgUrl(orderDetail.getImgUrl());
            orderInfo.setOperateTime(new Date());
            orderInfo.setOriginalTotalAmount(totalAmount);

            // 保存新订单
            this.save(orderInfo) ;

            // 保存新订单明细数据
            orderDetails.stream().map(od -> {
                OrderDetail newOrderDetail = new OrderDetail();
                BeanUtils.copyProperties(od, newOrderDetail);
                newOrderDetail.setId(null);
                newOrderDetail.setOrderId(orderInfo.getId());
                return newOrderDetail;
            }).forEach(e -> orderDetailMapper.insert(e));

            // 构建响应结果
            WareStockMsg wareStockMsg = new WareStockMsg() ;
            wareStockMsg.setOrderId(orderInfo.getId());
            wareStockMsg.setConsignee(orderInfo.getConsignee());
            wareStockMsg.setConsigneeTel(orderInfo.getConsigneeTel());
            wareStockMsg.setOrderComment(orderInfo.getOrderComment());
            wareStockMsg.setOrderBody(orderInfo.getTradeBody());
            wareStockMsg.setDeliveryAddress(orderInfo.getDeliveryAddress());
            wareStockMsg.setPaymentWay("2");
            List<Sku> skuList = orderDetails.stream().map(o -> {
                Sku sku = new Sku();
                sku.setSkuId(o.getSkuId());
                sku.setSkuNum(Integer.parseInt(o.getSkuNum()));
                sku.setSkuName(o.getSkuName());
                return sku;
            }).collect(Collectors.toList());
            wareStockMsg.setDetails(skuList);
            wareStockMsg.setWareId(wareSkuMapVo.getWareId());
            wareStockMsgList.add(wareStockMsg) ;

        }
        // 把之前的订单的状态改为已拆单
        if(OrderStatus.PAID.name().equals(oldOrderInfo.getOrderStatus()) && ProcessStatus.PAID.name().equals(oldOrderInfo.getProcessStatus())) {

            oldOrderInfo.setOrderStatus(OrderStatus.SPLIT.name());
            oldOrderInfo.setProcessStatus(ProcessStatus.SPLIT.name());

            LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>() ;
            lambdaQueryWrapper.eq(OrderInfo::getId , oldOrderInfo.getId()) ;
            lambdaQueryWrapper.eq(OrderInfo::getUserId , oldOrderInfo.getUserId()) ;
            lambdaQueryWrapper.eq(OrderInfo::getOrderStatus , OrderStatus.PAID.name()) ;
            lambdaQueryWrapper.eq(OrderInfo::getProcessStatus , ProcessStatus.PAID.name()) ;

            this.update(oldOrderInfo , lambdaQueryWrapper) ;

        }
        return wareStockMsgList;
    }

    //构建扣减库存消息
    private WareStockMsg buildWareStockMsg(OrderInfo orderInfo) {
        WareStockMsg wareStockMsg = new WareStockMsg();
        wareStockMsg.setOrderId(orderInfo.getId());
        wareStockMsg.setConsignee(orderInfo.getConsignee());
        wareStockMsg.setConsigneeTel(orderInfo.getConsigneeTel());
        wareStockMsg.setOrderComment(orderInfo.getOrderComment());
        wareStockMsg.setOrderBody(orderInfo.getTradeBody());
        wareStockMsg.setDeliveryAddress(orderInfo.getDeliveryAddress());
        wareStockMsg.setPaymentWay("2");

        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderDetail::getOrderId, orderInfo.getId());
        lambdaQueryWrapper.eq(OrderDetail::getUserId, orderInfo.getUserId());
        List<OrderDetail> orderDetails = orderDetailMapper.selectList(lambdaQueryWrapper);
        List<Sku> skuList = orderDetails.stream().map(orderDetail -> {
            Sku sku = new Sku();
            sku.setSkuId(orderDetail.getSkuId());
            sku.setSkuNum(Integer.valueOf(orderDetail.getSkuNum()));
            sku.setSkuName(orderDetail.getSkuName());
            return sku;
        }).collect(Collectors.toList());
        wareStockMsg.setDetails(skuList);

        return wareStockMsg;
    }

    private void saveOrderStatusLog(OrderInfo orderInfo) {
        // 创建OrderStatusLog对象封装要保存的数据
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setUserId(orderInfo.getUserId());
        orderStatusLog.setOrderId(orderInfo.getId());
        orderStatusLog.setOrderStatus(orderInfo.getOrderStatus());
        orderStatusLog.setOperateTime(new Date());

        // 保存
        orderStatusLogMapper.insert(orderStatusLog);
    }

    private OrderInfo updateOrderStatusPayed(PaymentInfo paymentInfo) {
        LambdaUpdateWrapper<OrderInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(OrderInfo::getId, paymentInfo.getOrderId());
        lambdaUpdateWrapper.eq(OrderInfo::getUserId, paymentInfo.getUserId());
        lambdaUpdateWrapper.eq(OrderInfo::getOrderStatus, OrderStatus.UNPAID.name())
                .or().eq(OrderInfo::getOrderStatus, OrderStatus.CLOSED.name());
        lambdaUpdateWrapper.eq(OrderInfo::getProcessStatus, ProcessStatus.UNPAID.name())
                .or().eq(OrderInfo::getProcessStatus, ProcessStatus.CLOSED.name());

        lambdaUpdateWrapper.set(OrderInfo::getOrderStatus, OrderStatus.PAID.name());
        lambdaUpdateWrapper.set(OrderInfo::getProcessStatus, ProcessStatus.PAID.name());
        update(lambdaUpdateWrapper);
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderInfo::getId, paymentInfo.getOrderId());
        lambdaQueryWrapper.eq(OrderInfo::getUserId, paymentInfo.getUserId());
        return getOne(lambdaQueryWrapper);
    }

    private String getOrderStatusById(String orderId, Long userId) {
        return orderInfoMapper.getOrderStatusById(orderId, userId);
    }

    private PaymentInfo savePaymentInfo(Map<String, String> map) {
        //获取外部交易号,查询订单信息
        String outTradeNo = map.get("out_trade_no");
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderInfo::getOutTradeNo, outTradeNo);
        OrderInfo orderInfo = this.getOne(lambdaQueryWrapper);
        //创建PaymentInfo封装保存的数据
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(outTradeNo);
        paymentInfo.setOrderId(String.valueOf(orderInfo.getId()));
        paymentInfo.setPaymentType("ALIPAY");
        paymentInfo.setTradeNo(map.get("trade_no"));
        paymentInfo.setTotalAmount(orderInfo.getTotalAmount());
        paymentInfo.setSubject(orderInfo.getTradeBody());
        paymentInfo.setPaymentStatus(PaymentStatus.PAID.name());
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(map));
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfoMapper.insert(paymentInfo);
        return paymentInfo;
    }
}
