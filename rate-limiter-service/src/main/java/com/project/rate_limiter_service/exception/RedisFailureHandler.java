package com.project.rate_limiter_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisFailureHandler {
    public boolean handle(Exception e, String algorithm) {
        log.error("Redis unavailable during {} rate limiting. Failing open temporarily.", algorithm, e);
        return true;
    }
}