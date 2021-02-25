package com.chanbook.rate.limiter.rule;

public interface RateLimiter {
    /**
     * 限流判断
     * @param appId
     * @param api
     * @return
     */
    boolean limit(String appId, String api);
}
