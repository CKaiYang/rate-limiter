package com.chanbook.rate.limiter.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * RuleConfig
 *
 * @author chanbook
 * @date 2021/2/25 10:34 下午
 */
@Data
public class RuleConfig {
    List<AppRuleConfig> configs;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AppRuleConfig {
        private String appId;
        private List<ApiLimit> limits;
    }
}
