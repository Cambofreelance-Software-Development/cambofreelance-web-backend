package com.cambofreelance.authenticationservice.caches;

import com.cambofreelance.authenticationservice.repository.CmsSettingRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpWhitelistCache {

    private final CmsSettingRepository repository;

    private volatile boolean enabled = false;
    private volatile List<String> rules = List.of();
    private volatile long lastLoaded = 0L;
    private static final long TTL_MS = 30_000L;

    public boolean isEnabled() {
        maybeRefresh();
        return enabled;
    }

    public List<String> getRules() {
        maybeRefresh();
        return rules;
    }

    public synchronized void refresh() {
        enabled = "true".equalsIgnoreCase(loadKey("ip_whitelist_enabled"));
        String raw = loadKey("ip_whitelist_rules");
        rules = !raw.isBlank()
            ? Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList()
            : List.of();
        lastLoaded = System.currentTimeMillis();
        log.debug("IP whitelist refreshed: enabled={}, rules={}", enabled, rules);
    }

    private synchronized void maybeRefresh() {
        if (System.currentTimeMillis() - lastLoaded > TTL_MS) {
            refresh();
        }
    }

    private String loadKey(String key) {
        return repository.findBySettingKey(key)
            .map(e -> e.getSettingValue() != null ? e.getSettingValue() : "")
            .orElse("");
    }
}
