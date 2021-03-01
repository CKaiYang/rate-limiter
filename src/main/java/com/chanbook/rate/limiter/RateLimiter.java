package com.chanbook.rate.limiter;

import com.chanbook.rate.limiter.alg.FixTimeWindowAlg;
import com.chanbook.rate.limiter.alg.RateLimitAlg;
import com.chanbook.rate.limiter.datasource.FileRuleConfigSource;
import com.chanbook.rate.limiter.datasource.RuleConfigSource;
import com.chanbook.rate.limiter.rule.ApiLimit;
import com.chanbook.rate.limiter.rule.RateLimitRule;
import com.chanbook.rate.limiter.rule.RuleConfig;
import com.chanbook.rate.limiter.rule.TrieRateLimitRule;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ReteLimiter
 *
 * @author chanbook
 * @date 2021/2/25 10:33 下午
 */
@Slf4j
public class RateLimiter {
    private final Map<String, RateLimitAlg> counters = new ConcurrentHashMap<>();
    private final RateLimitRule rule;

    public RateLimiter() {
        RuleConfigSource ruleConfigSource = new FileRuleConfigSource();
        RuleConfig ruleConfig = ruleConfigSource.load();
        assert ruleConfig != null;
        this.rule = new TrieRateLimitRule(ruleConfig);
    }

    /**
     * @param appId
     * @param url
     * @return true 可通过 false 不可通过
     */
    public boolean limit(String appId, String url) {
        ApiLimit apiLimit = rule.getLimit(appId, url);
        if (apiLimit == null) {
            return true;
        }

        String counterKey = getKey(appId, apiLimit.getApi());
        return counters.computeIfAbsent(counterKey, key -> new FixTimeWindowAlg(apiLimit)).tryAcquire();
    }

    private String getKey(String appId, String api) {
        return String.format("%s:%s", appId, api);
    }
}
