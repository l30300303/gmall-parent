package com.liu.gmall.cart.service;
/*
 *@title CartService
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/19 19:58
 */


import com.liu.gmall.cart.entity.CartItem;
import com.liu.gmall.cart.vo.AddCartSuccessVo;

import java.util.List;

public interface CartService {
    AddCartSuccessVo addCart(Long skuId, Integer skuNum);

    List<CartItem> cartList();

    void addToCart(Long skuId, Integer skuNum);

    void checkCart(Long skuId, Integer isChecked);

    void deleteCart(Long skuId);

    void deleteCheckedCart();

    List<CartItem> findByUserId();
}
