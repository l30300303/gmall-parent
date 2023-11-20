package com.liu.gmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.service.CartService;
import com.liu.gmall.cart.vo.AddCartSuccessVo;
import com.liu.gmall.common.auth.UserAuthInfo;
import com.liu.gmall.common.execption.GmallException;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.common.result.ResultCodeEnum;
import com.liu.gmall.common.utils.UserAuthUtils;
import com.liu.gmall.feign.product.SkuDetailFeignClient;
import com.liu.gmall.product.entity.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public AddCartSuccessVo addCart(Long skuId, Integer skuNum) {
        //针对用户是否登录构建不同的redis key
        String redisKey = buildRedisCartKey();
        Result<SkuInfo> skuInfoByIdResult = skuDetailFeignClient.getSkuInfoById(skuId);
        SkuInfo skuInfo = skuInfoByIdResult.getData();

        //在存储数据到redis之前需要先判断当前商品在redis中是否存在
        Boolean hasKey = redisTemplate.opsForHash().hasKey(redisKey, String.valueOf(skuId));
        if (hasKey) {
            String cartItemJson = redisTemplate.opsForHash().get(redisKey, String.valueOf(skuId)).toString();
            CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
            cartItem.setSkuNum(cartItem.getSkuNum() + skuNum);
            cartItem.setSkuPrice(skuInfo.getPrice());
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(redisKey, String.valueOf(skuId), JSON.toJSONString(cartItem));
        } else {
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
        // 判断用户是否操作的是临时购物车，如果是临时购物车设置一个有效期
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        String userId = userAuthInfo.getUserId();
        if(StringUtils.isEmpty(userId)) {       // 未登录
            String userTempCartKey = "cart:info:" + userAuthInfo.getUserTempId() ;
            Long expire = redisTemplate.getExpire(userTempCartKey);
            if(expire < 0) {        // -1(永不过期)
                redisTemplate.expire(userTempCartKey , 30 , TimeUnit.DAYS) ;
            }
        }
        return addCartSuccessVo;
    }

    @Override
    public List<CartItem> cartList() {
        //需要在用户登录之后查看购物车的时候将临时用户和登录用户的购物车数据进行合并
        UserAuthInfo userAuthInfo = UserAuthUtils.getUserAuthInfo();
        String userId = userAuthInfo.getUserId();
        //userId不为null，用户已登录
        if (!StringUtils.isEmpty(userId)) {
            // 用户购物车的数据key
            String userCartKey = "cart:info:" + userId;
            // 获取用户的临时购物车数据
            String userTempIdCartKey = "cart:info:" + userAuthInfo.getUserTempId();
            List<Object> objectList = redisTemplate.opsForHash().values(userTempIdCartKey);
            List<CartItem> userTempCartItemList = objectList.stream().map(obj -> JSON.parseObject(obj.toString(), CartItem.class)).collect(Collectors.toList());
            List<Object> objects = redisTemplate.opsForHash().values(userCartKey);
            List<CartItem> userCartItemList = objects.stream().map(obj -> JSON.parseObject(obj.toString(), CartItem.class)).collect(Collectors.toList());
            long count = Stream.concat(userCartItemList.stream().map(cartItem -> cartItem.getSkuId()), userTempCartItemList.stream().map(cartItem -> cartItem.getSkuId())).count();
            //对购物车数量进行限制
            if (count>10){
                throw new GmallException(ResultCodeEnum.CART_ITEM_COUNT_ERROR);
            }
            // 遍历临时购物车的list集合
            for(CartItem cartItem : userTempCartItemList) {

                // 判断当前的购物项在用户购物车中是否存在，如果不存在直接添加进入，如果存在购买数量进行累加
                Boolean hasKey = redisTemplate.opsForHash().hasKey(userCartKey, String.valueOf(cartItem.getSkuId()));
                if(hasKey) {
                    String userCartItemJSON = redisTemplate.opsForHash().get(userCartKey, String.valueOf(cartItem.getSkuId())).toString();
                    CartItem userCartItem = JSON.parseObject(userCartItemJSON, CartItem.class);
                    userCartItem.setSkuNum(userCartItem.getSkuNum() + cartItem.getSkuNum());
                    Integer skuNum = userCartItem.getSkuNum();
                    if(skuNum > 10) {
                        userCartItem.setSkuNum(10);
                    }
                    redisTemplate.opsForHash().put(userCartKey , String.valueOf(cartItem.getSkuId()) , JSON.toJSONString(userCartItem));
                }else {     // 不存在
                    redisTemplate.opsForHash().put(userCartKey , String.valueOf(cartItem.getSkuId()) , JSON.toJSONString(cartItem));
                }

            }

            // 删除临时购物车
            redisTemplate.delete(userTempIdCartKey) ;

            // 查询用户购物车的数据，进行返回
            return findAllCartItem(userCartKey) ;
        } else {        //用户未登录
            String redisCartKey = buildRedisCartKey();
            return findAllCartItem(redisCartKey) ;
        }
    }

    private List<CartItem> findAllCartItem(String redisKey) {

        List<Object> list = redisTemplate.opsForHash().values(redisKey);
        List<CartItem> cartItemList = list.stream().map(obj -> {
            String cartItemJSON = obj.toString();
            CartItem cartItem = JSON.parseObject(cartItemJSON, CartItem.class);

            // 远程调用service-product微服务的接口查询商品价格数据
            Result<SkuInfo> skuInfoResult = skuDetailFeignClient.getSkuInfoById(cartItem.getSkuId());
            SkuInfo skuInfo = skuInfoResult.getData();
            cartItem.setSkuPrice(skuInfo.getPrice());       // 实现价格同步
            // 更改Redis中的商品的价格
            redisTemplate.opsForHash().put(redisKey , String.valueOf(cartItem.getSkuId()) , JSON.toJSONString(cartItem));
            return cartItem;
        }).sorted((o1 , o2) -> (int)(o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime())).collect(Collectors.toList());

        return cartItemList ;

    }

    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        String redisCartKey = buildRedisCartKey();
        String cartItemJson = Objects.requireNonNull(redisTemplate.opsForHash().get(redisCartKey, String.valueOf(skuId))).toString();
        CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
        cartItem.setSkuNum(cartItem.getSkuNum() + skuNum);
        //对商品数量进行限制
        if (cartItem.getSkuNum() > 10) {
            cartItem.setSkuNum(10);
        }
        redisTemplate.opsForHash().put(redisCartKey, String.valueOf(skuId), JSON.toJSONString(cartItem));
    }

    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        String redisCartKey = buildRedisCartKey();
        String cartItemJson = Objects.requireNonNull(redisTemplate.opsForHash().get(redisCartKey, String.valueOf(skuId))).toString();
        CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
        cartItem.setIsChecked(isChecked);
        redisTemplate.opsForHash().put(redisCartKey, String.valueOf(skuId), JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteCart(Long skuId) {
        String redisCartKey = buildRedisCartKey();
        redisTemplate.opsForHash().delete(redisCartKey, String.valueOf(skuId));
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
