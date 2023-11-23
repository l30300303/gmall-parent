package com.liu.gmall.common.retryer;
/*
 *@title MyRetryer
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/23 9:44
 */


import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientRetryer implements Retryer {
    private int start = 1;

    private final int end = 2;

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.info("=====执行了feign自定义重试器=====");
        if (end > start) {
            throw new RuntimeException(e);
        }
        start++;
    }

    @Override
    public Retryer clone() {
        return new FeignClientRetryer();
    }
}
