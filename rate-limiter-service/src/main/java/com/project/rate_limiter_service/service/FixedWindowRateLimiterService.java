package com.project.rate_limiter_service.service;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.exception.RedisFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FixedWindowRateLimiterService implements RateLimiterStrategy {
    private final StringRedisTemplate redisTemplate;
    private final RedisFailureHandler redisFailureHandler;
    @Value("${rate-limiter.fixed-window.max-requests}")
    private int maxRequests;
    @Value("${rate-limiter.fixed-window.window-seconds}")
    private int windowSeconds;
    private final RedisScript<Long> fixedWindowScript;
    @Override
    public boolean isAllowed(String clientId) {
        try {
            String key = "rate_limit:" + clientId;
            Long result =
                    redisTemplate.execute(
                            fixedWindowScript,
                            Collections.singletonList(key),
                            String.valueOf(maxRequests),
                            String.valueOf(windowSeconds)
                    );
            return result != null && result == 1;
        }
        catch (Exception e) {
            return redisFailureHandler.handle(e, "FIXED_WINDOW");
        }
    }

    @Override
    public String getAlgorithmName() {
        return "FIXED_WINDOW";
    }
}