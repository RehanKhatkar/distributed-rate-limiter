package com.project.rate_limiter_service.controller;

import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.service.RateLimiterStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequiredArgsConstructor
public class ConcurrencyTestController {
    private final RateLimiterStrategyFactory factory;
    @GetMapping("/concurrency-test")
    public String concurrencyTest() throws Exception {
        String clientId = "concurrent-user";
        int totalRequests = 20;
        RateLimiterStrategy strategy = factory.getStrategy("FIXED_WINDOW");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < totalRequests; i++) {
            Future<Boolean> future = executorService.submit(() -> strategy.isAllowed(clientId));
            futures.add(future);
        }
        int allowed = 0;
        int blocked = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                allowed++;
            } else {
                blocked++;
            }
        }
        executorService.shutdown();
        return """
                Total Requests: %d
                Allowed: %d
                Blocked: %d
                """
                .formatted(totalRequests, allowed, blocked);
    }
}