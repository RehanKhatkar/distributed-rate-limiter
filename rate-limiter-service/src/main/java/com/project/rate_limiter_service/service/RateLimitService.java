package com.project.rate_limiter_service.service;

public interface RateLimitService {
    boolean isAllowed(String clientId);
}