package com.redis.api.services;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.csc.Cache;


/**
 * RedisService is a child of {@link UnifiedJedis} wich can use all of his 
 * methods and properties
 * 
 * @see UnifiedJedis 
 * 
 * @author Guilherme Enache
*/
public class RedisService extends UnifiedJedis{
    public RedisService(HostAndPort node, JedisClientConfig clientConfig, Cache cache) {
        super(node, clientConfig, cache);
    }
    
}

