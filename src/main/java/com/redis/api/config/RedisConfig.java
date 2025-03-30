package com.redis.api.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redis.api.services.RedisService;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.csc.Cache;
import redis.clients.jedis.csc.CacheConfig;
import redis.clients.jedis.csc.CacheFactory;
import redis.clients.jedis.csc.Cacheable;
import redis.clients.jedis.csc.DefaultCacheable;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;


    /**
     * A bean to instantiate RedisService
     * 
     * @see RedisService
     * 
     * @return a RedisService configuration
     * @throws InterruptedException
     * 
     * @author Guilherme Enache
     */
    @Bean
    RedisService redisService() throws InterruptedException{
        HostAndPort node = HostAndPort.from(host + ":" + port);
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .resp3()
                .build();
            
        CacheConfig cacheConfig = getCacheConfig();

        Cache cache = CacheFactory.getCache(cacheConfig);

        return new RedisService(node, clientConfig, cache);
    }

    /**
     * <p>Redis cachce config carry:</p>
     * <p>Search Method, Max size of keys and values etc...</p>
     * 
     * @return a Cache configuration
     * 
     * @author Guilherme Enache
     */
    private CacheConfig getCacheConfig() {

        // This is a simple cacheable implementation that ignores keys starting with
        // "ignore_me"
        Cacheable cacheable = new DefaultCacheable() {

            final String IGNORE_PREFIX = "ignore_me";

            @Override
            public boolean isCacheable(ProtocolCommand command, List<Object> keys) {
                List<String> stringKeys = keys.stream()
                        .filter(obj -> obj instanceof String)
                        .map(obj -> (String) obj)
                        .collect(Collectors.toList());

                for (String key : stringKeys) {
                    if (key.startsWith(IGNORE_PREFIX)) {
                        return false;
                    }
                }

                return isDefaultCacheableCommand(command);
            }
        };

        // Create a cache with a maximum size of 10000 entries
        return CacheConfig.builder()
                .maxSize(10000)
                .cacheable(cacheable)
                .build();
    }

}
