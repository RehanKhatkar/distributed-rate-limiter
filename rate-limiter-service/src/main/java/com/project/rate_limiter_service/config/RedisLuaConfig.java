package com.project.rate_limiter_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
@Configuration
public class RedisLuaConfig {
    @Bean
    public DefaultRedisScript<Long> fixedWindowScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(
                new ClassPathResource(
                        "lua/fixed_window.lua"
                )
        );
        script.setResultType(Long.class);
        return script;
    }
}