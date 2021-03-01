package com.chanbook.rate.limiter.parser;

import com.chanbook.rate.limiter.rule.RuleConfig;

import java.io.InputStream;

public interface RuleConfigParser {
    RuleConfig parse(String configText);
    RuleConfig parse(InputStream in);
}
