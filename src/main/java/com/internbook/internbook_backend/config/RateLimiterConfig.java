package com.internbook.internbook_backend.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.internbook.internbook_backend.services.TokenBucketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RateLimiterConfig {
    @Value("${spring.caffeine.expiration}")
    private long expiration;

    @Value("${spring.caffeine.maximumSize}")
    private long maximumSize;

    private final RateLimiterProperties rateLimiterProperties;

    public RateLimiterConfig(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    @Bean
    public TokenBucketService tokenBucketService() {
        return new TokenBucketService(rateLimiterProperties.getCapacity(), rateLimiterProperties.getRefillRate());
    }

    @Bean
    public Cache<String, TokenBucketService> ipBuckets () {
        return Caffeine.newBuilder()
                .expireAfterAccess(expiration, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .build();
    }
}
