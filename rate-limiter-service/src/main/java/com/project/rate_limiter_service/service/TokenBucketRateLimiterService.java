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
public class TokenBucketRateLimiterService implements RateLimiterStrategy {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> tokenBucketScript;
    private final RedisFailureHandler redisFailureHandler;
    @Value("${rate-limiter.token-bucket.capacity}")
    private int capacity;
    @Value("${rate-limiter.token-bucket.refill-rate}")
    private int refillRate;
    public boolean isAllowed(String clientId) {
        try {
            String key = "token_bucket:" + clientId;
            long currentTime = Instant.now().getEpochSecond();
            Long result =
                    redisTemplate.execute(
                            tokenBucketScript,
                            Collections.singletonList(key),
                            String.valueOf(capacity),
                            String.valueOf(refillRate),
                            String.valueOf(currentTime)
                    );
            return result != null && result == 1;
        }
        catch (Exception e) {
            return redisFailureHandler.handle(e, "TOKEN_BUCKET");
        }
    }
    @Override
    public String getAlgorithmName() {
        return "TOKEN_BUCKET";
    }
}