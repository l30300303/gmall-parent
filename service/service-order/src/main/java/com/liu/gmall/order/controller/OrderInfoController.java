package com.liu.gmall.order.controller;
/*
 *@title OrderInfoController
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 18:50
 */


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.gmall.common.result.Result;
import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.entity.OrderInfo;
import com.liu.gmall.order.service.OrderInfoService;
import com.liu.gmall.ware.entity.WareStockMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * @param tradeNo
     * @param orderSubmitDTO
     * @return
     * @Valid 该注解表示开启数据校验
     */
    @PostMapping("/auth/submitOrder")
    public Result<String> submitOrder(@RequestParam("tradeNo") String tradeNo, @Valid @RequestBody OrderSubmitDto orderSubmitDTO) {
        String orderId = orderInfoService.submitOrder(tradeNo, orderSubmitDTO);
        return Result.ok(orderId);
    }

    @GetMapping("/auth/{pageNum}/{pageSize}")
    public Result<Page<OrderInfo>> getOrder(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Page<OrderInfo> page = orderInfoService.getOrder(pageNum, pageSize);
        return Result.ok(page);
    }

    @PostMapping("/orderSplit")
    public List<WareStockMsg> orderSplit(@RequestParam("orderId") Long orderId, @RequestParam("wareSkuMap") String wareSkuMap) {
        return orderInfoService.orderSplit(orderId,wareSkuMap);
    }
}
