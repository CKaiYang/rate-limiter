package com.chanbook.rate.limiter.alg;

import com.chanbook.rate.limiter.rule.ApiLimit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * RateLimiterAlg
 *
 * @author chanbook
 * @date 2021/2/25 11:55 下午
 */
public class RateLimiterAlg {
    private final static int INITIAL = 0;
    private final ApiLimit apiLimit;
    private final AtomicLong count = new AtomicLong(INITIAL);
    private Long lastTime = System.currentTimeMillis();

    public RateLimiterAlg(ApiLimit apiLimit) {
        this.apiLimit = apiLimit;
    }

    public boolean tryAcquire() {
        long currTime = System.currentTimeMillis();
        /**
         * 是否超过时间
         */
        boolean overTime = currTime - lastTime > apiLimit.getUnit();
        if (overTime) {
            count.set(INITIAL);
            lastTime = currTime;
        } else if (count.get() > apiLimit.getLimit()) {
            return false;
        }
        count.incrementAndGet();
        return true;
    }
}
