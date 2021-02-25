package com.chanbook.rate.limiter.rule;

import java.util.List;

public interface RateLimiterRuleParser {
    /**
     * 根据路径解析限流规则
     * @param path
     * @return
     */
    List<RuleInfo> parse(String path);
}
