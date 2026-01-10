package com.internbook.internbook_backend.services;

public class TokenBucketService {
    private final long capacity;
    private long tokens;
    private final long refillRatePerSecond;
    private long lastRefillTimeStamp;

    public TokenBucketService (long capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillTimeStamp = System.nanoTime();
    }

    public synchronized boolean tryConsume() {
        refillIfNeeded();
        if(tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refillIfNeeded () {
        long now = System.nanoTime();
        long elapsedTime = (now - lastRefillTimeStamp) / 1_000_000_000;
        if(elapsedTime > 0) {
            long newTokens = elapsedTime *  refillRatePerSecond;
            tokens = Math.min(capacity, newTokens);
            lastRefillTimeStamp = now;
        }
    }
}
