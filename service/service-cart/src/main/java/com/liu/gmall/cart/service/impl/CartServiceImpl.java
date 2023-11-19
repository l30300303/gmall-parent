package com.liu.gmall.cart.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/*
 *@title CartServiceImpl
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 19:58
 */


import com.alibaba.fastjson.JSON;
import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.service.CartService;
import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.product.entity.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public AddCartSuccessVo addCart(Long skuId, Integer skuNum) {
        //针对用户是否登录构建不同的redis key
        String redisKey = buildRedisCartKey();
        Result<SkuInfo> skuInfoByIdResult = skuDetailFeignClient.getSkuInfoById(skuId);
        SkuInfo skuInfo = skuInfoByIdResult.getData();

        //在存储数据到redis之前需要先判断当前商品在redis中是否存在
        Boolean hasKey = redisTemplate.opsForHash().hasKey(redisKey, String.valueOf(skuId));
        if (hasKey){
            String cartItemJson = redisTemplate.opsForHash().get(redisKey, String.valueOf(skuId)).toString();
            CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
            cartItem.setSkuNum(cartItem.getSkuNum() + skuNum);
            cartItem.setSkuPrice(skuInfo.getPrice());
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(redisKey, String.valueOf(skuId), JSON.toJSONString(cartItem));
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setSkuId(skuId);
            cartItem.setCartPrice(skuInfo.getPrice());
            cartItem.setSkuPrice(skuInfo.getPrice());
            cartItem.setSkuNum(skuNum);
            cartItem.setImgUrl(skuInfo.getSkuDefaultImg());
            cartItem.setSkuName(skuInfo.getSkuName());
            cartItem.setIsChecked(1);
            cartItem.setCreateTime(new Date());
            cartItem.setUpdateTime(new Date());

            redisTemplate.opsForHash().put(redisKey, String.valueOf(skuId), JSON.toJSONString(cartItem));
        }

        AddCartSuccessVo addCartSuccessVo = new AddCartSuccessVo();
        addCartSuccessVo.setSkuInfo(skuInfo);
        addCartSuccessVo.setSkuNum(skuNum);
        return addCartSuccessVo;
    }

    @Override
    public List<CartItem> cartList() {
        String redisCartKey = buildRedisCartKey();
        List<Object> values = redisTemplate.opsForHash().values(redisCartKey);
        List<CartItem> cartItemList = values.stream().map(obj -> JSON.parseObject(obj.toString(), CartItem.class))
               .sorted((c1,c2)-> (int) (c2.getUpdateTime().getTime() - c1.getUpdateTime().getTime()))
                .collect(Collectors.toList());
        return cartItemList;
    }

    private String buildRedisCartKey() {
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        if (!StringUtils.isEmpty(userAuthInfo.getUserId())) {
            return "cart:info:" + userAuthInfo.getUserId();
        } else {
            return "cart:info:" + userAuthInfo.getUserTempId();
        }
    }
}
