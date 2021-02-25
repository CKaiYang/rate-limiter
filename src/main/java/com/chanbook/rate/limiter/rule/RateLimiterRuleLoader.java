package com.chanbook.rate.limiter.rule;

public interface RateLimiterRuleLoader {
    /**
     * 加载所有限流规则
     *
     * @return
     */
    boolean loadAll();

    /**
     * 重新加载所有限流规则
     *
     * @return
     */
    boolean reload();

    /**
     * 更新限流规则
     *
     * @return
     */
    boolean update(String appId, String api);
}
