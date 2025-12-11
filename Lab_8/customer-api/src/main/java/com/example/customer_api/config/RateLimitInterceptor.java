package com.example.customer_api.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// BONUS 3: Rate Limiting Logic
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) throws Exception {
        
        String key = request.getRemoteAddr(); // Limit by IP Address
        Bucket bucket = cache.computeIfAbsent(key, k -> createNewBucket());
        
        if (bucket.tryConsume(1)) {
            return true; // Request allowed
        }
        
        // Request rejected
        response.setStatus(429); // 429 Too Many Requests
        response.getWriter().write("Too many requests - Rate limit exceeded");
        return false;
    }
    
    private Bucket createNewBucket() {
        // Limit: 100 requests per 1 minute
        // NOTE: For testing, you can change this to simple(2, Duration.ofMinutes(1))
        return Bucket.builder()
            .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
            .build();
    }
}