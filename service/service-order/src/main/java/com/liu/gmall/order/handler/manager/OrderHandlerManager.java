package com.liu.gmall.order.handler.manager;
/*
 *@title OrderHandlerManager
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/22 19:54
 */


import com.liu.gmall.order.dto.OrderSubmitDto;
import com.liu.gmall.order.handler.AbstractOrderHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderHandlerManager implements ApplicationContextAware {

    // 定义list集合管理订单操作的所有的步骤
    private List<AbstractOrderHandler> orderHandlerList = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractOrderHandler> orderHandlerMap = applicationContext.getBeansOfType(AbstractOrderHandler.class);
        orderHandlerList = orderHandlerMap.values().stream().sorted((o1, o2) -> o1.sort() - o2.sort()).collect(Collectors.toList());
        for (int i = 0; i < orderHandlerList.size(); i++) {
            if (i == orderHandlerList.size() - 1) {
                orderHandlerList.get(i).setNext(null);
            } else {
                orderHandlerList.get(i).setNext(orderHandlerList.get(i + 1));
            }
        }
    }

    // 责任链步骤的执行入口
    public String exec(OrderSubmitDto orderSubmitDto, String tradeNo) {
        return orderHandlerList.get(0).process(orderSubmitDto, tradeNo);
    }
}
