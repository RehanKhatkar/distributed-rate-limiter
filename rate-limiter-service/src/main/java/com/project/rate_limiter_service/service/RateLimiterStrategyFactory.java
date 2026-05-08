package com.project.rate_limiter_service.service;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimiterStrategyFactory {
    private final List<RateLimiterStrategy> strategies;
    private final Map<String, RateLimiterStrategy> strategyMap = new HashMap<>();
    @PostConstruct
    public void init() {
        for (RateLimiterStrategy strategy : strategies) {
            strategyMap.put(strategy.getAlgorithmName(), strategy);
        }
    }
    public RateLimiterStrategy getStrategy(String algorithm)
    {
        RateLimiterStrategy strategy = strategyMap.get(algorithm);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
        }
        return strategy;
    }
}