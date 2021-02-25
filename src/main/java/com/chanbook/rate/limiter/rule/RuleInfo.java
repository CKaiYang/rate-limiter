package com.chanbook.rate.limiter.rule;

import lombok.Builder;

/**
 * 限流规则
 */
public class RuleInfo {
    /**
     * 调用方
     */
    protected String appId;
    /**
     * 限流接口
     */
    protected String api;
    /**
     * 限制次数
     */
    protected Long limit;
    /**
     * 时间范围
     * 单位：s 秒
     */
    @Builder.Default
    protected Long unit = 1L;
}
