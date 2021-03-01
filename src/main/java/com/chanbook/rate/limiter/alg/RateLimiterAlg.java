package com.chanbook.rate.limiter.alg;

import com.chanbook.rate.limiter.rule.ApiLimit;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RateLimiterAlg
 *
 * @author chanbook
 * @date 2021/2/25 11:55 下午
 */
@Slf4j
public class RateLimiterAlg {
    private final static int INITIAL = 0;
    private final static int LOCK_TIME = 20;
    private final ApiLimit apiLimit;
    private final AtomicLong count = new AtomicLong(INITIAL);
    private final StopWatch stopWatch = new StopWatch();
    private final Lock lock = new ReentrantLock();

    public RateLimiterAlg(ApiLimit apiLimit) {
        this.apiLimit = apiLimit;
    }

    /**
     * 并发问题
     * 1、当在边界时 线程1和线程2同时处于 count.get() >= apiLimit.getLimit()
     * 2、线程1正要执行 count.incrementAndGet(); 时，线程2 重置了时间
     *
     * @return true 可以访问 false 不能访问
     */
    public boolean tryAcquire() {
        try {
            lock.tryLock(LOCK_TIME, TimeUnit.MILLISECONDS);
            try {
                /**
                 * 是否超过时间
                 */
                if (stopWatch.elapsed() > apiLimit.getUnit()) {
                    count.set(INITIAL);
                    stopWatch.reset();
                } else if (count.get() >= apiLimit.getLimit()) {
                    return false;
                }
                count.incrementAndGet();
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            log.error("{},限流算法加锁失败/n,{}", this.getClass().getSimpleName(), e.getMessage());
        }
        return true;
    }

    private static class StopWatch {
        private long lastTime = System.currentTimeMillis();

        public void reset() {
            lastTime = System.currentTimeMillis();
        }

        public long elapsed() {
            long currTime = System.currentTimeMillis();
            return currTime - lastTime;
        }
    }

}
