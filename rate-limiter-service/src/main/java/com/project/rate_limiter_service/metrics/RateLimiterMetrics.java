package com.project.rate_limiter_service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterMetrics {
    private final MeterRegistry registry;
    public RateLimiterMetrics(MeterRegistry registry) {
        this.registry = registry;
    }
    public void incrementAllowed(String algorithm) {
        Counter.builder(
                        "rate_limiter_allowed_requests"
                )
                .tag(
                        "algorithm",
                        algorithm
                )
                .description(
                        "Total allowed requests"
                )
                .register(registry)
                .increment();
    }
    public void incrementBlocked(String algorithm) {
        Counter.builder(
                        "rate_limiter_blocked_requests"
                )
                .tag(
                        "algorithm",
                        algorithm
                )
                .description(
                        "Total blocked requests"
                )
                .register(registry)
                .increment();
    }
}