package com.cambofreelance.webbackend.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * In-memory sliding-window rate limiter for the public contact form.
 * Allows at most {@link #MAX_PER_WINDOW} submissions per client IP
 * within {@link #WINDOW_MILLIS}. State is per-instance; a restart resets it,
 * which is acceptable for abuse protection on a contact form.
 */
@Component
public class ContactRateLimiter {

    private static final int  MAX_PER_WINDOW = 5;
    private static final long WINDOW_MILLIS  = 15 * 60 * 1000L;
    private static final int  MAX_TRACKED_IPS = 10_000;

    private final Map<String, Deque<Long>> hitsByIp = new ConcurrentHashMap<>();

    /** Returns true if the request is allowed and records it; false if the IP is over the limit. */
    public boolean tryAcquire(String ip) {
        long now = System.currentTimeMillis();
        if (hitsByIp.size() > MAX_TRACKED_IPS) {
            pruneStale(now);
        }
        Deque<Long> hits = hitsByIp.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (hits) {
            while (!hits.isEmpty() && now - hits.peekFirst() > WINDOW_MILLIS) {
                hits.pollFirst();
            }
            if (hits.size() >= MAX_PER_WINDOW) {
                return false;
            }
            hits.addLast(now);
            return true;
        }
    }

    private void pruneStale(long now) {
        hitsByIp.entrySet().removeIf(e -> {
            Deque<Long> hits = e.getValue();
            synchronized (hits) {
                return hits.isEmpty() || now - hits.peekLast() > WINDOW_MILLIS;
            }
        });
    }
}
