package com.chanbook.rate.limiter.rule;

import java.util.List;

public interface RateLimiterRuleReader {
    /**
     * 获取所有限流规则
     *
     * @return
     */
    List<RuleInfo> getAllRuleInfo();

    /**
     * 获取某个调用方限流规则
     *
     * @param appId
     * @return
     */
    List<RuleInfo> getRuleInfoByAppId(String appId);

    /**
     * 获取具体的限流规则
     *
     * @param appId
     * @param api
     * @return
     */
    RuleInfo getRuleInfoByAppIdAndApi(String appId, String api);
}
