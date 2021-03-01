package com.chanbook.rate.limiter.alg;

public interface RateLimitAlg {
    /**
     * 限流判断
     * @return true 可以访问  false 不能访问
     */
    boolean tryAcquire();
}
