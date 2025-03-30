package com.redis.api.interceptor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redis.api.services.RedisService;
import com.redis.api.util.HttpResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestFilter implements Filter {

    // Is the list of HTTP methods to clear Redis keys
    private final List<String> clearMethods = 
        new ArrayList<>(List.of("POST", "DELETE", "PUT"));
    
    // Methods to save response in Cache
    //
    // Obs: If you need can be more than just GET
    private final List<String> cacheableMethods = 
        new ArrayList<>(List.of("GET"));

    @Autowired
    private RedisService redis;

    /**
     * Saves the response body on cache(currently Redis) if it's not in cache
     * 
     * @param key
     * @param responseBody
     * 
     * @author Guilherme Enache Caetano
     */
    private void saveCache(String key, String responseBody) {
        String cacheValue = redis.get(key);
        if(cacheValue == null) {
            redis.set(key, responseBody);
        }
    }

    /**
     * Delete all cache data if exists
     * 
     * @param key
     * 
     * @author Guilherme Enache Caetano
     */
    private void clearCache(String key) {
        String cacheValue = redis.get(key);
        if(cacheValue != null) {
            redis.del(key);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {// No initialization required
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = ((HttpServletRequest) request);

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(httpResponse);

        chain.doFilter(request, responseWrapper);

        byte[] responseData = responseWrapper.getResponseData();
        String responseBody = new String(responseData, httpResponse.getCharacterEncoding());

        String redisKey = httpRequest.getRequestURI();
        String httpMethod = httpRequest.getMethod();
        
        // In the case you need save or clear in cache
        if(cacheableMethods.contains(httpMethod)) {
            saveCache(redisKey, responseBody);
        } else if(clearMethods.contains(httpMethod)) {
            clearCache(redisKey);
        }

        response.getOutputStream().write(responseData);
    }


    @Override
    public void destroy() {
    }
}