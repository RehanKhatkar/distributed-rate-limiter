package com.project.rate_limiter_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Collections;
@Service
@RequiredArgsConstructor
public class TokenBucketRateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> tokenBucketScript;
    private static final int CAPACITY = 10;
    private static final int REFILL_RATE = 1;
    public boolean isAllowed(String clientId) {
        String key = "token_bucket:" + clientId;
        long currentTime = Instant.now().getEpochSecond();
        Long result =
                redisTemplate.execute(
                        tokenBucketScript,
                        Collections.singletonList(key),
                        String.valueOf(CAPACITY),
                        String.valueOf(REFILL_RATE),
                        String.valueOf(currentTime)
                );
        return result != null && result == 1;
    }
}