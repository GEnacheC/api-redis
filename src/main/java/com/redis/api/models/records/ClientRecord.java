package com.redis.api.models.records;

/**
 * Record to save Clients
 * 
 * @author Guilherme Enache Caetano
 */
public record ClientRecord(
    String name,
    Integer age
) {
}
