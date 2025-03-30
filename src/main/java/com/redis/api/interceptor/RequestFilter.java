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

/**
 * Filter to capture and log the response body.
 */
@Component
public class RequestFilter implements Filter {

    @Autowired
    private RedisService redis;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void doFilter(
            ServletRequest request, 
            ServletResponse response, 
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        
        // Cast response to HttpServletResponse
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // Wrap the response to capture the body
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(httpResponse);
        // Continue with the filter chain
        chain.doFilter(request, responseWrapper);
        // Get the response body data from the wrapper
        byte[] responseData = responseWrapper.getResponseData();
        String responseBody = new String(responseData, httpResponse.getCharacterEncoding());
        
        // Log the response body
        System.out.println("Response Body: " + responseBody);
        
        if(httpRequest.getMethod().equals("GET")) {
            if(redis.get(httpRequest.getRequestURI()) == null) {
                redis.set(httpRequest.getRequestURI(), responseBody);
            }
        }
        
        // Write the response body back to the client
        response.getOutputStream().write(responseData);
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}