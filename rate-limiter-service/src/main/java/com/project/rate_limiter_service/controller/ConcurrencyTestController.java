package com.project.rate_limiter_service.controller;
import com.project.rate_limiter_service.algorithm.RateLimiterStrategy;
import com.project.rate_limiter_service.metrics.RateLimiterMetrics;
import com.project.rate_limiter_service.service.RateLimiterStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
@RestController
@RequiredArgsConstructor
public class ConcurrencyTestController {
    private final RateLimiterStrategyFactory factory;
    private final RateLimiterMetrics metrics;
    @GetMapping("/concurrency-test/{algorithm}")
    public String concurrencyTest(
            @PathVariable String algorithm,
            @RequestParam(defaultValue = "20")
            int totalRequests,
            @RequestParam(defaultValue = "10")
            int threadPoolSize
    ) throws Exception {
        String clientId = "concurrent-user";
        RateLimiterStrategy strategy =
                factory.getStrategy(
                        algorithm.toUpperCase()
                );
        ExecutorService executorService =
                Executors.newFixedThreadPool(
                        threadPoolSize
                );
        List<Future<Boolean>> futures =
                new ArrayList<>();
        for (int i = 0; i < totalRequests; i++) {
            Future<Boolean> future =
                    executorService.submit(
                            () -> strategy.isAllowed(clientId)
                    );
            futures.add(future);
        }
        int allowed = 0;
        int blocked = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                allowed++;
                metrics.incrementAllowed();
            } else {
                blocked++;
                metrics.incrementBlocked();
            }
        }
        executorService.shutdown();
        return """
                Algorithm: %s
                Total Requests: %d
                Thread Pool Size: %d
                Allowed: %d
                Blocked: %d
                """
                .formatted(
                        algorithm.toUpperCase(),
                        totalRequests,
                        threadPoolSize,
                        allowed,
                        blocked
                );
    }
}