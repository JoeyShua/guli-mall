package com.jxs.coupon.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArrayList;


@RestController
@RequestMapping("/hystrix1")
@DefaultProperties(defaultFallback = "defaultFail")
public class HystrixController {

    /*@HystrixCommand(commandProperties  = {
            @HystrixProperty(name = "circuitBreaker.enabled" ,value = "true"), //开启熔断降级功能
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value ="5"),   //最小请求次数，这里是5个请求
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds" , value ="5000"),  //熔断时间5秒
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value = "50")  //错误流程比例

    } ,fallbackMethod = "fail1")*/
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            //@HystrixProperty(name = "execution.isolation.thread.maxConcurrentRequests",value = "5"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000000000")
    }, fallbackMethod = "fail1",
            commandKey = "queryContents",
            groupKey = "querygroup-one")
    @GetMapping("/test1/{num}")
    public String test1(@PathVariable("num") int num) {
        if (num % 2 == 0) {
            return "正常访问";
        }else{
            throw new RuntimeException("sss");
        }

       /* try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "正常访问";*/
    }

    private String fail1(int num) {
        System.out.println("fail1");
        return "fail1";
    }

    @HystrixCommand(fallbackMethod = "fail2")
    @GetMapping("/test2")
    public String test2() {
        throw new RuntimeException();
    }

    @HystrixCommand(fallbackMethod = "fail3")
    private String fail2() {
        System.out.println("fail2");
        throw new RuntimeException();
    }

    @HystrixCommand
    private String fail3() {
        System.out.println("fail3");
        throw new RuntimeException();
    }

    private String defaultFail() {
        System.out.println("default fail");
        return "default fail";
    }
}
