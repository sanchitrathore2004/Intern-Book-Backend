package com.internbook.internbook_backend.filters;

import com.github.benmanes.caffeine.cache.Cache;
import com.internbook.internbook_backend.config.RateLimiterProperties;
import com.internbook.internbook_backend.services.TokenBucketService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimiterFilter extends HttpFilter {
    private final TokenBucketService globalBucket;
    private final Cache<String, TokenBucketService> ipBuckets;
    private final RateLimiterProperties rateLimiterProperties;

    protected void doFilter (HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain chain) throws IOException, ServletException {

        String clientIp = request.getRemoteAddr();
        TokenBucketService ipBucket = ipBuckets.get(clientIp, k->new TokenBucketService(
                rateLimiterProperties.getPerIpCapacity(),
                rateLimiterProperties.getPerIpRefillRate()
        ));

        if (!ipBucket.tryConsume()) {
            response.setStatus(429);
            response.getWriter().write("Too many requests, try after a few seconds.");
            return;
        }

        if (!globalBucket.tryConsume()) {
            response.setStatus(429);
            response.getWriter().write("Too many requests.");
            return;
        }

        chain.doFilter(request, response);
    }
}
