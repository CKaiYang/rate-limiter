package com.chanbook.rate.limiter;

import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
public class TestRateLimiter {

    @Test
    public void limitTest() throws InterruptedException {
        RateLimiter limiter = new RateLimiter();
        String appId = "app-1";
        String api = "/v1/user/info";
        long begin = System.currentTimeMillis();
        int numThreads = 5000;
        CountDownLatch countDownLatch = new CountDownLatch(numThreads);
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger count2 = new AtomicInteger(0);
        for (int i = 0; i < numThreads; i++) {
            new Thread(()->{
                boolean limit = limiter.limit(appId, api);
                if (limit) {
                    count.incrementAndGet();
                }
                countDownLatch.countDown();
                count2.incrementAndGet();
            }).start();
        }
        countDownLatch.await();
        System.out.println("执行时间:" + (System.currentTimeMillis() - begin) + ",通过次数:" + count.get());
        System.out.println("执行次数："+count2.get());
    }
}
