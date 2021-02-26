package com.chanbook.rate.limiter;

import com.chanbook.rate.limiter.alg.RateLimiterAlg;
import com.chanbook.rate.limiter.rule.ApiLimit;
import com.chanbook.rate.limiter.rule.RateLimitRule;
import com.chanbook.rate.limiter.rule.RuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
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
    private Map<String, RateLimiterAlg> counters = new ConcurrentHashMap<>();
    private RateLimitRule rule;

    public RateLimiter() {
        InputStream in = null;
        RuleConfig ruleConfig = null;
        try {
            in = this.getClass().getResourceAsStream("/rate-limiter.yaml");
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, RuleConfig.class);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.rule = new RateLimitRule(ruleConfig);
    }

    public boolean limit(String appId, String url) {
        ApiLimit apiLimit = rule.getLimit(appId, url);
        if (apiLimit == null) {
            return true;
        }

        String counterKey = String.format("%s:%s", appId, apiLimit.getApi());
        RateLimiterAlg rateLimiterAlg = counters.get(counterKey);
        if (rateLimiterAlg == null) {
            RateLimiterAlg newRateLimiterAlg = new RateLimiterAlg();
            rateLimiterAlg = counters.putIfAbsent(counterKey, newRateLimiterAlg);
            if (rateLimiterAlg == null) {
                rateLimiterAlg = newRateLimiterAlg;
            }
        }
        return rateLimiterAlg.tryAcquire();
    }
}
