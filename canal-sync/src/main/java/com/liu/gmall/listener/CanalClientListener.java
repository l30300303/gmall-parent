package com.liu.gmall.listener;
/*
 *@title CanalClientListener
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/10 20:02
 */


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.UpdateListenPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@CanalEventListener
@Slf4j
public class CanalClientListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @UpdateListenPoint(schema = "gmall_product", table = "sku_info")
    public void updateMethod(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        log.info("时间：：eventType::{}", eventType);

        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        afterColumnsList.forEach(column -> {
            if ("id".equals(column.getName())) {
                log.info("sku:info:{}", column.getValue());
                redisTemplate.delete("sku:info:" + column.getValue());
            }
        });
    }
}
