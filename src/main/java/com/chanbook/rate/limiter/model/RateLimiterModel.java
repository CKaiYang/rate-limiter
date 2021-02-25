package com.chanbook.rate.limiter.model;

public interface RateLimiterModel {
    /**
     * 限流判断
     * @param appId
     * @param api
     * @return
     */
    boolean limit(String appId, String api);
}
