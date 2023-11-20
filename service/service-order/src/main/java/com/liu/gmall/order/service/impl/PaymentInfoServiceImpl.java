package com.liu.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.gmall.order.entity.PaymentInfo;
import com.liu.gmall.order.service.PaymentInfoService;
import com.liu.gmall.order.mapper.PaymentInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author L3030
* @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
* @createDate 2023-11-20 21:29:23
*/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
    implements PaymentInfoService{

}




