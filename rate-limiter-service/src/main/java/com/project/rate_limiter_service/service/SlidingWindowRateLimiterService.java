package com.project.rate_limiter_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SlidingWindowRateLimiterService{

    private final StringRedisTemplate redisTemplate;

    private final RedisScript<Long> slidingWindowScript;

    private static final int MAX_REQUESTS = 5;

    private static final int WINDOW_SECONDS = 60;

    public boolean isAllowed(String clientId) {

        String key = "sliding_window:" + clientId;

        long currentTime = Instant.now().getEpochSecond();

        Long result =
                redisTemplate.execute(
                        slidingWindowScript,
                        Collections.singletonList(key),
                        String.valueOf(currentTime),
                        String.valueOf(WINDOW_SECONDS),
                        String.valueOf(MAX_REQUESTS)
                );

        return result != null && result == 1;
    }
}