package com.chanbook.rate.limiter.datasource;

import com.chanbook.rate.limiter.rule.RuleConfig;

public interface RuleConfigSource {

    /**
     * 加载限流规则配置
     * @return
     */
    RuleConfig load();
}
