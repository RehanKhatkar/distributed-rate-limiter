package com.project.rate_limiter_service.filter;

import com.project.rate_limiter_service.service.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {
    private final RateLimitService rateLimitService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String clientId = resolveClientId(httpRequest);
        boolean allowed = rateLimitService.isAllowed(clientId);
        if (!allowed) {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Rate limit exceeded");
            return;
        }
        chain.doFilter(request, response);
    }
    private String resolveClientId(HttpServletRequest request) {
        String clientId = request.getHeader("X-Client-Id");
        return clientId != null ? clientId : request.getRemoteAddr();
    }
}