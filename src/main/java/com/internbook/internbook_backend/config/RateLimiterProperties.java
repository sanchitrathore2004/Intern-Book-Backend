package com.internbook.internbook_backend.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RateLimiterProperties {
    @Value("${spring.rateLimiter.capacity}")
    private long capacity;

    @Value("${spring.rateLimiter.refillRate}")
    private long refillRate;

    @Value("${spring.rateLimiter.perIp.capacity}")
    private long perIpCapacity;

    @Value("${spring.rateLimiter.perIp.refillRate}")
    private long perIpRefillRate;
}
