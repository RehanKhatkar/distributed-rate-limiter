package com.project.rate_limiter_service.controller;

import com.project.rate_limiter_service.service.TokenBucketRateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenBucketController {
    private final TokenBucketRateLimiterService service;
    @GetMapping("/token-bucket-test")
    public String test(@RequestHeader("X-Client-Id") String clientId) {
        boolean allowed = service.isAllowed(clientId);
        return allowed
                ? "Request Allowed"
                : "Rate Limit Exceeded";
    }
}