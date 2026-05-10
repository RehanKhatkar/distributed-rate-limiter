package com.project.rate_limiter_service.controller;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.metrics.RateLimiterMetrics;
import com.project.rate_limiter_service.service.RateLimiterStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rate-limit")
@RequiredArgsConstructor
public class RateLimiterController {
    private final RateLimiterStrategyFactory factory;
    private final RateLimiterMetrics metrics;
    @GetMapping("/{algorithm}")
    public String test(@PathVariable String algorithm, @RequestHeader("X-Client-Id") String clientId)
    {
        RateLimiterStrategy strategy = factory.getStrategy(algorithm.toUpperCase());
        boolean allowed = strategy.isAllowed(clientId);
        if (allowed) {
            metrics.incrementAllowed();
            return "Request Allowed";
        }
        metrics.incrementBlocked();
        return "Rate Limit Exceeded";
    }
}