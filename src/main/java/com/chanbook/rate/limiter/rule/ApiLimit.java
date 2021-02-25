package com.chanbook.rate.limiter.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ApiLimit
 *
 * @author chanbook
 * @date 2021/2/25 10:33 下午
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiLimit {
    private static final Long DEFAULT_TIME_UNIT = 1L;

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
     * 单位：ms 毫秒
     */
    @Builder.Default
    protected Long unit = DEFAULT_TIME_UNIT;
}
