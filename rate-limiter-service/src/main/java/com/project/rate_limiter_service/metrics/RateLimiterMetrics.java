package com.project.rate_limiter_service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterMetrics {
    private final Counter allowedRequests;
    private final Counter blockedRequests;
    public RateLimiterMetrics(MeterRegistry registry) {
        this.allowedRequests = Counter.builder("rate_limiter_allowed_requests_total").description("Total allowed requests").register(registry);
        this.blockedRequests = Counter.builder("rate_limiter_blocked_requests_total").description("Total blocked requests").register(registry);
    }
    public void incrementAllowed() {
        allowedRequests.increment();
    }
    public void incrementBlocked() {
        blockedRequests.increment();
    }
}