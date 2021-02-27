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
    private final Map<String, RateLimiterAlg> counters = new ConcurrentHashMap<>();
    private final RateLimitRule rule;

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
        assert ruleConfig != null;
        this.rule = new RateLimitRule(ruleConfig);
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
        return counters.computeIfAbsent(counterKey, key -> new RateLimiterAlg(apiLimit)).tryAcquire();
    }

    private String getKey(String appId, String api) {
        return String.format("%s:%s", appId, api);
    }
}
