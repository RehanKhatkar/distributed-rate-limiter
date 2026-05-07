package com.project.rate_limiter_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FixedWindowRateLimiterService implements RateLimitService {
    private final StringRedisTemplate redisTemplate;
    private static final int MAX_REQUESTS = 5;
    private static final int WINDOW_SECONDS = 60;
    private final RedisScript<Long> fixedWindowScript;
    @Override
    public boolean isAllowed(String clientId) {
        String key = "rate_limit:" + clientId;
        Long result =
                redisTemplate.execute(
                        fixedWindowScript,
                        Collections.singletonList(key),
                        String.valueOf(MAX_REQUESTS),
                        String.valueOf(WINDOW_SECONDS)
                );

        return result != null && result == 1;
    }
}