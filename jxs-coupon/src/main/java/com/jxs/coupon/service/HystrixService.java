package com.jxs.coupon.service;

public interface HystrixService {

    public String error() ;

    public String timeOut() ;


    String get();
}
