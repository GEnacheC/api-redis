package com.redis.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.api.models.Client;
import com.redis.api.repository.ClientRepository;

@Service
public class ClientService {
    
    @Autowired
    ClientRepository repository;

    @Autowired
    RedisService redis;

    public List<Client> getAll() {

        redis.get("");

        return repository.findAll();
    }

}
