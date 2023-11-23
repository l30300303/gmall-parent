package com.liu.gmall.feign.user;
/*
 *@title UserAddressFeignClient
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 20:45
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.user.fallback.UserAddressFeignClientFallback;
import com.liu.gmall.user.entity.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-user",fallback = UserAddressFeignClientFallback.class)
public interface UserAddressFeignClient {

    @GetMapping(value = "/api/inner/user/findByUserId")
    public Result<List<UserAddress>> findByUserId();
}
