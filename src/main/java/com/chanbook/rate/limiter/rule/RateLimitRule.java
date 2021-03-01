package com.chanbook.rate.limiter.rule;

/**
 * RateLimitRule
 *
 * @author chanbook
 * @date 2021/2/25 10:36 下午
 */
public interface RateLimitRule {

    /**
     * 获取限流规则
     * @param appId 调用方
     * @param url 调用url
     * @return null 无限流规则
     */
    ApiLimit getLimit(String appId, String url);
}
