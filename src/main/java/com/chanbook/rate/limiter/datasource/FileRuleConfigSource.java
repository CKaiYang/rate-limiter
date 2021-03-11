package com.chanbook.rate.limiter.datasource;

import com.chanbook.rate.limiter.parser.JsonRuleConfigParser;
import com.chanbook.rate.limiter.parser.RuleConfigParser;
import com.chanbook.rate.limiter.parser.YamlRuleConfigParser;
import com.chanbook.rate.limiter.rule.RuleConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地文件加载限流规则
 */
public class FileRuleConfigSource implements RuleConfigSource {
    public static final String API_LIMIT_CONFIG_NAME = "rate-limiter-rule";
    public static final String YAML_EXTENSION = "yaml";
    public static final String YML_EXTENSION = "yml";
    public static final String JSON_EXTENSION = "json";

    private static final String[] SUPPORT_EXTENSIONS = new String[]{YAML_EXTENSION, YML_EXTENSION, JSON_EXTENSION};

    private static final Map<String, RuleConfigParser> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put(YAML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(YML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(JSON_EXTENSION, new JsonRuleConfigParser());
    }

    @Override
    public RuleConfig load() {
        for (String extension : SUPPORT_EXTENSIONS) {
            InputStream in = null;
            try {
                in = this.getClass().getResourceAsStream("/" + getFileNameByExt(API_LIMIT_CONFIG_NAME, extension));
                if (in != null) {
                    RuleConfigParser parser = PARSER_MAP.get(extension);
                    return parser.parse(in);
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
        }
        return null;
    }

    private String getFileNameByExt(String configName, String extension) {
        return String.format("%s.%s", configName, extension);
    }
}
