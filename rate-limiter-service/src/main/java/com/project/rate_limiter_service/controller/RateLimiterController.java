package com.project.rate_limiter_service.controller;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.metrics.RateLimiterMetrics;
import com.project.rate_limiter_service.service.RateLimiterStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
@RestController
@RequestMapping("/rate-limit")
@RequiredArgsConstructor
public class RateLimiterController {
    private final RateLimiterStrategyFactory factory;
    private final RateLimiterMetrics metrics;
    @GetMapping("/{algorithm}")
    public ResponseEntity<Map<String, Object>> test(@PathVariable String algorithm, @RequestHeader("X-Client-Id") String clientId) {
        RateLimiterStrategy strategy = factory.getStrategy(algorithm.toUpperCase());
        boolean allowed = strategy.isAllowed(clientId);
        if (allowed) {
            metrics.incrementAllowed(algorithm);
            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
            response.put("status", "success");
            response.put("message", "Request Allowed");
            response.put("algorithm", algorithm);
            return ResponseEntity.ok(response);
        }else{
            metrics.incrementBlocked(algorithm);
            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
            response.put("status", "blocked");
            response.put("message", "Rate Limit Exceeded");
            response.put("algorithm", algorithm);

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(response);
        }
    }
}