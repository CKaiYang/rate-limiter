package com.chanbook.rate.limiter.parser;

import com.chanbook.rate.limiter.rule.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlRuleConfigParser implements RuleConfigParser {
    @Override
    public RuleConfig parse(String configText) {
        return null;
    }

    @Override
    public RuleConfig parse(InputStream in) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(in, RuleConfig.class);
    }
}
