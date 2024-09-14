package com.hsbc.incident.util;

import java.util.*;
import java.util.concurrent.*;

public class ExpiringCache<K, V> {
    private final ConcurrentHashMap<K, CacheObject<V>> cacheMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Constructor to automatically clean up expired entries periodically
    public ExpiringCache(long cleanupInterval) {
        scheduler.scheduleAtFixedRate(this::cleanup, cleanupInterval, cleanupInterval, TimeUnit.MILLISECONDS);
    }

    // CacheObject class that holds the value and the expiry time
    private static class CacheObject<V> {
        private final V value;
        private final long expiryTime;

        CacheObject(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        V getValue() {
            return value;
        }
    }

    // Put a value into the cache with an expiration time
    public void put(K key, V value, long ttl) {
        long expiryTime = System.currentTimeMillis() + ttl;
        cacheMap.put(key, new CacheObject<>(value, expiryTime));
    }

    // Get the value from the cache, or return null if expired or not present
    public V get(K key) {
        CacheObject<V> cacheObject = cacheMap.get(key);
        if (cacheObject == null || cacheObject.isExpired()) {
            cacheMap.remove(key); // remove if expired
            return null;
        }
        return cacheObject.getValue();
    }

    // Remove a specific key from the cache
    public void remove(K key) {
        cacheMap.remove(key);
    }

    // Clean up expired entries
    private void cleanup() {
        for (K key : cacheMap.keySet()) {
            CacheObject<V> cacheObject = cacheMap.get(key);
            if (cacheObject != null && cacheObject.isExpired()) {
                cacheMap.remove(key);
            }
        }
    }

    // Shutdown the scheduler when the cache is no longer needed
    public void shutdown() {
        scheduler.shutdown();
    }
}
