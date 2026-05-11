package com.project.rate_limiter_service.service;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.exception.RedisFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SlidingWindowRateLimiterService implements RateLimiterStrategy {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> slidingWindowScript;
    private final RedisFailureHandler redisFailureHandler;
    @Value("${rate-limiter.sliding-window.max-requests}")
    private int maxRequests;
    @Value("${rate-limiter.sliding-window.window-seconds}")
    private int windowSeconds;
    public boolean isAllowed(String clientId) {
        try {
            String key = "sliding_window:" + clientId;
            long currentTime = Instant.now().getEpochSecond();
            Long result =
                    redisTemplate.execute(
                            slidingWindowScript,
                            Collections.singletonList(key),
                            String.valueOf(currentTime),
                            String.valueOf(windowSeconds),
                            String.valueOf(maxRequests)
                    );
            return result != null && result == 1;
        }
        catch (Exception e) {
            return redisFailureHandler.handle(e, "SLIDING_WINDOW");
        }
    }

    @Override
    public String getAlgorithmName() {
        return "SLIDING_WINDOW";
    }
}