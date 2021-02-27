package com.chanbook.rate.limiter.alg;

import com.chanbook.rate.limiter.rule.ApiLimit;
import lombok.SneakyThrows;
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
    private final ApiLimit apiLimit;
    private final AtomicLong count = new AtomicLong(INITIAL);
    private Lock lock = new ReentrantLock();

    public RateLimiterAlg(ApiLimit apiLimit) {
        this.apiLimit = apiLimit;
    }

    @SneakyThrows
    public boolean tryAcquire() {
        long currTime = System.currentTimeMillis();
        /**
         * 是否超过时间
         */
        boolean overTime = currTime - lastTime > apiLimit.getUnit();
        if (lock.tryLock(20, TimeUnit.MILLISECONDS)) {
            try {
                if (overTime) {
                    count.set(INITIAL);
                    lastTime = currTime;
                } else if (count.get() >= apiLimit.getLimit()) {
                    return false;
                }
                count.incrementAndGet();
            }finally {
                lock.unlock();
            }

        } else {
            log.error("wait lock to long");
            return false;
        }

        return true;
    }

    private static class StopWatch{
        private long lastTime = System.currentTimeMillis();
        public void reset(){
            lastTime = System.currentTimeMillis();
        }
    }
}
