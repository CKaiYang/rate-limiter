package com.chanbook.rate.limiter.rule;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RateLimitRule
 *
 * @author chanbook
 * @date 2021/2/25 10:36 下午
 */
public class RateLimitRule {
    private final static String SPLIT = "/";
    private final Map<String, Trie> appIdTrie = new ConcurrentHashMap<>();

    public RateLimitRule(RuleConfig ruleConfig) {
        List<RuleConfig.AppRuleConfig> configs = ruleConfig.getConfigs();
        for (RuleConfig.AppRuleConfig config : configs) {
            Trie trie = appIdTrie.computeIfAbsent(config.getAppId(), key -> new Trie());
            List<ApiLimit> limits = config.getLimits();
            for (ApiLimit limit : limits) {
                trie.insert(limit.getApi(), limit);
            }
        }
    }

    public ApiLimit getLimit(String appId, String api) {
        Trie trie = appIdTrie.get(appId);
        if (Objects.isNull(trie)) {
            return null;
        }
        TrieNode trieNode = trie.getTrieNode(api);
        if (Objects.isNull(trieNode)) {
            return null;
        }
        return trieNode.getLimit();
    }

    public void insert(String appId, String api, ApiLimit apiLimit) {
        Trie trie = appIdTrie.computeIfAbsent(appId, key -> new Trie());
        trie.insert(api, apiLimit);
    }

    public void update(String appId, String api, ApiLimit apiLimit) {
        Trie trie = appIdTrie.computeIfAbsent(appId, key -> new Trie());
        trie.update(api, apiLimit);
    }

    private static class Trie {
        private final TrieNode root;

        public Trie() {
            this.root = new TrieNode("root");
        }

        public void insert(String api, ApiLimit apiLimit) {
            TrieNode curr = createTrieNode(api);
            if (Objects.isNull(curr.getLimit())) {
                curr.setLimit(apiLimit);
            }
        }

        public void update(String api, ApiLimit apiLimit) {
            TrieNode curr = createTrieNode(api);
            curr.setLimit(apiLimit);
        }

        private TrieNode createTrieNode(String api) {
            TrieNode curr = this.root;
            String[] split = api.split(SPLIT);
            for (String key : split) {
                if (StringUtils.isEmpty(key)) {
                    continue;
                }
                curr = curr.getNextNodes().computeIfAbsent(key, c -> new TrieNode(key));
            }
            return curr;
        }

        public TrieNode getTrieNode(String api) {
            TrieNode curr = this.root;
            String[] split = api.split(SPLIT);
            for (String key : split) {
                if (StringUtils.isEmpty(key)) {
                    continue;
                }
                if (curr == null) {
                    return null;
                }
                curr = curr.getNextNodes().get(key);
            }
            return curr;
        }
    }

    private static class TrieNode {
        private final static int DEFAULT_INIT_CAP = 16;
        @Getter
        private final String key;
        private final Map<String, TrieNode> nextNodes = new ConcurrentHashMap<>(DEFAULT_INIT_CAP);
        @Setter
        @Getter
        private ApiLimit limit;

        public TrieNode(String key) {
            this.key = key;
        }

        public Map<String, TrieNode> getNextNodes() {
            return nextNodes;
        }
    }
}
