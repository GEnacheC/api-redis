package com.redis.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.redis.api.interceptor.RequestInterceptor;
import com.redis.api.services.RedisService;

@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RedisService redis;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(redis));
    }
}

