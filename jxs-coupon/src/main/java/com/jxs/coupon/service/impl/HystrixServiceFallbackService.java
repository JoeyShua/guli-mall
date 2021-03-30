package com.jxs.coupon.service.impl;

import com.jxs.coupon.service.HystrixService;
import org.springframework.stereotype.Service;

@Service
public class HystrixServiceFallbackService implements HystrixService {
    @Override
    public String error() {

        return "报错了，进行服务降级";
    }

    @Override
    public String timeOut() {
        return "超时了，进行服务降级";
    }

    @Override
    public String get() {
        return null;
    }
}
