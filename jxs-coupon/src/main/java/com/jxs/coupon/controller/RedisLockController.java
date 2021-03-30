package com.jxs.coupon.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("coupon/lock")
public class RedisLockController {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStock() {

        /*Boolean hasKey = redisTemplate.hasKey("stock");
        if (!hasKey) {
            redisTemplate.opsForValue().set("stock", "200");
        }*/
        try {
            lock5();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "end";
    }


    /**
     * 1.从redis 中拿到数据
     * 2.操作数据
     * 3.把操作结果放到redis中
     * <p>
     * 结果：在集群/分布式环境中严重出现扣减重复的情况
     */
    private void lock1() {
        int stock = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get("stock")));

        if (stock > 0) {
            int realStock = stock - 1;
            redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
            System.out.println("扣减库存成功，剩余库存：" + realStock);
        } else {
            System.out.println("扣减库存失败");
        }
    }


    /**
     * 改进点：
     * 使用setIfAbsent，当不在时，set，当存在时，不做任何操作
     * <p>
     * 存问题点：
     * 如果中间业务代码出现报错，将不会出现解锁情况，其他也就都拿不到锁
     */
    private void lock2() {
        String localKey = "aaadd";
        String lockValue = "lockValue";
        Boolean isExists = redisTemplate.opsForValue().setIfAbsent(localKey, lockValue);
        if (!isExists) {
            System.out.println("没有拿到锁");
            return;
        }
        int stock = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get("stock")));

        //业务代码开始。。。。。。
        int aa = 1 / 0;
        //业务代码结束。。。。。。
        if (stock > 0) {
            int realStock = stock - 1;
            redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
            System.out.println("扣减库存成功，剩余库存：" + realStock);
        } else {
            System.out.println("扣减库存失败");
        }
        redisTemplate.delete(localKey);
    }


    /**
     * 改进点：
     * 在key 上增加超时时间，中间业务代码报错后，超时后会自动解锁
     * <p>
     * 问题点：
     * 如果中间业务时间超过了 设置的超时时间,即，业务还没结束，第一个线程的锁被第二个线程的锁解锁了
     */
    private void lock3() {
        String localKey = "localKey";
        String lockValue = "lockValue";
        Boolean isExists = redisTemplate.opsForValue().setIfAbsent(localKey, lockValue, 3, TimeUnit.SECONDS);
        if (!isExists) {
            System.out.println("没有拿到锁");
            return;
        }
        int stock = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get("stock")));

        //业务代码开始。。。。。。
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //业务代码结束。。。。。。
        if (stock > 0) {
            int realStock = stock - 1;
            redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
            System.out.println("扣减库存成功，剩余库存：" + realStock);
        } else {
            System.out.println("扣减库存失败");
        }
        redisTemplate.delete(localKey);
    }


    /**
     * 改进点：
     * 在value 增加了唯一标识
     * <p>
     * 问题点：
     * 锁设置超时的超时时间，不管设置多长都是不合适的
     * 当业务逻辑处理时间比 设置锁的超时时间长， 就会出现当前线程还没执行完， 下个线程就会进入到业务，会出现问题
     *
     */
    private void lock4() {
        String localKey = "localKey";
        String lockValue = UUID.randomUUID().toString();

        try {
            Boolean isExists = redisTemplate.opsForValue().setIfAbsent(localKey, lockValue, 3, TimeUnit.SECONDS);
            if (!isExists) {
                System.out.println("没有拿到锁");
                return;
            }
            int stock = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get("stock")));

            //业务代码开始。。。。。。
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //业务代码结束。。。。。。
            if (stock > 0) {
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
                System.out.println("扣减库存成功，剩余库存：" + realStock);
            } else {
                System.out.println("扣减库存失败");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (lockValue.equals(redisTemplate.opsForValue().get("stock"))) {
                redisTemplate.delete(localKey);
            }
        }
    }


    /**
     * 使用redisson
     */
    private void lock5() throws InterruptedException {

        RLock lock = redissonClient.getLock("ddd");
        //尝试对name进行加锁,如果该锁被其他线程持有,会等待10秒,然后返回是否成功,如果成功 会在20秒后自动解锁
        boolean b = lock.tryLock(10L, 20L, TimeUnit.SECONDS);
        //对name进行加锁 线程会一直等待 直到拿到该锁
      /*  lock.lock();
        //尝试对name进行加锁,线程会一直等待 直到拿到该锁 然后10秒后自动解锁
        lock.lock(10L,TimeUnit.SECONDS);*/
        //对name进行解锁,如果锁不是该线程持有则会抛出异常
        lock.unlock();
        /*//强制对name进行解锁,即此锁不论是那个线程持有都会进行解锁
        lock.forceUnlock();


        //尝试对name进行加锁 立即返回加锁状态 如果加锁成功会在20秒后自动解锁
        boolean b1 = lock.tryLock(20L, TimeUnit.SECONDS);
        //检查该锁是否被任何线程所持有
        boolean locked = lock.isLocked();
        //检查该锁是否当前线程持有
        boolean heldByCurrentThread = lock.isHeldByCurrentThread();
        //当前线程对该锁的保持次数
        int holdCount = lock.getHoldCount();
        //该锁的剩余时间
        long l = lock.remainTimeToLive();*/
    }
}
