package com.chanbook.rate.limiter.rule;

import java.util.Objects;

/**
 * RateLimitRule
 *
 * @author chanbook
 * @date 2021/2/25 10:36 下午
 */
public class RateLimitRule {
    private final static String SPLIT = "/";

    public RateLimitRule(RuleConfig ruleConfig) {

    }

    public ApiLimit getLimit(String appId, String api) {
        return null;
    }

    public boolean add(String api, ApiLimit apiLimit,Node head) {
        Node node = head;
        String[] split = api.split(SPLIT);
        for (String s : split) {
            if (node.hasNext()){
                return false;
            }

            node = node.next();
        }
    }

    private static class Node {
        private String value;
        private ApiLimit limit;
        private Node next;

        public boolean hasNext() {
            return Objects.nonNull(next);
        }

        public Node next() {
            return next;
        }
    }
}
