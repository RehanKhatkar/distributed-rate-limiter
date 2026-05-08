package com.project.rate_limiter_service.controller;

import com.project.rate_limiter_service.service.SlidingWindowRateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SlidingWindowController {

    private final SlidingWindowRateLimiterService service;

    @GetMapping("/sliding-test")
    public String test(@RequestHeader("X-Client-Id") String clientId) {
        boolean allowed = service.isAllowed(clientId);
        return allowed
                ? "Request Allowed"
                : "Rate Limit Exceeded";
    }
}