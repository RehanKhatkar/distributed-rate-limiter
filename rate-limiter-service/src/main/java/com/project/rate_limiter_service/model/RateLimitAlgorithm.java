package com.project.rate_limiter_service.model;

public enum RateLimitAlgorithm {

    FIXED_WINDOW,
    SLIDING_WINDOW,
    TOKEN_BUCKET
}