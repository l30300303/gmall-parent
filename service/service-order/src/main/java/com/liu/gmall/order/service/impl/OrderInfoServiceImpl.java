package com.liu.gmall.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.feign.cart.CartFeignClient;
import com.liu.gmall.feign.user.UserAddressFeignClient;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.handler.manager.OrderHandlerManager;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.order.mapper.OrderInfoMapper;
import com.liu.gmall.order.vo.DetailVo;
import com.liu.gmall.order.vo.OrderConfirmVo;
import com.liu.gmall.user.entity.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
        lambdaQueryWrapper.eq(OrderInfo::getUserId,Long.valueOf(userAuthInfo.getUserId()));
        Page<OrderInfo> page = new Page<>(pageNum,pageSize);
        return page(page, lambdaQueryWrapper);
    }
}
