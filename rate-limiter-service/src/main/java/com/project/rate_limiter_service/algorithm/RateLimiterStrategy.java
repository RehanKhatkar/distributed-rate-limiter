package com.project.rate_limiter_service.algorithm;

public interface RateLimiterStrategy {

    boolean isAllowed(String clientId);

    String getAlgorithmName();
}