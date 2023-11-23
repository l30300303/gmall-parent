package com.liu.gmall.feign.user.fallback;
/*
 *@title UserAddressFeignClientFallback
 *@description
 *@author L3030
 *@version 1.0
 *@create 2023/11/21 20:46
 */


import com.liu.gmall.common.result.Result;
import com.liu.gmall.feign.user.UserAddressFeignClient;
import com.liu.gmall.user.entity.UserAddress;

import java.util.List;

public class UserAddressFeignClientFallback implements UserAddressFeignClient {
    @Override
    public Result<List<UserAddress>> findByUserId() {
        return Result.ok();
    }
}
