package com.jxs.coupon.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLockTool {


    @Autowired
    private RedissonClient redissonClient;


    //枷锁标志
    private static final String LOCK_SUCCESS = "OK";
    //不存在则加锁，存在则不加锁
    private static final String SET_IF_NOT_EXIST = "NX";
    //失效了才会加锁
    private static final String SET_WITH_EXPIRE_TIME = "PX";


    public  RLock tryGetDistributedLock(String lockKey, int expireTime){
        RLock lock=redissonClient.getLock(lockKey);
        lock.lock(expireTime, TimeUnit.SECONDS);
        return lock;
    }


}
