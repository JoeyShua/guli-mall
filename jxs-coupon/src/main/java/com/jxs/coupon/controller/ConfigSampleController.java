package com.jxs.coupon.controller;

import com.jxs.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("coupon/config")
public class ConfigSampleController {

    @Value("${coupon.user.name}")
    String userName;

    @Value("${coupon.user.age}")
    int age;

    @RequestMapping("/test")
    public R test(){

        return R.ok().put("userName", userName).put("age",age);
    }


}
