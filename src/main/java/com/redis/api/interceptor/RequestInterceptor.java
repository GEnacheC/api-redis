package com.redis.api.interceptor;

import java.nio.charset.StandardCharsets;

import org.springframework.web.servlet.HandlerInterceptor;

import com.redis.api.services.RedisService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {

    private RedisService redis;

    public RequestInterceptor(RedisService redis) {
        this.redis = redis;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {
        
        if(request.getMethod().equals("GET")) {
            String redisValue = redis.get(request.getRequestURI());
            if(redisValue != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json; charset=UTF-8");
                response.getOutputStream().write(redisValue.getBytes(StandardCharsets.UTF_8));
                return false;
            }
        }
        return true;
    }

}
