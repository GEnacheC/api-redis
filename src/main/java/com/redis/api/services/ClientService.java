package com.redis.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.api.models.Client;
import com.redis.api.models.records.ClientRecord;
import com.redis.api.repository.ClientRepository;

@Service
public class ClientService {
    
    @Autowired
    ClientRepository repository;

    @Autowired
    RedisService redis;

    public List<Client> getAll() {
        return repository.findAll();
    }

    public Client save(ClientRecord data) {
        return repository.save(new Client(data.name(), data.age()));
    }

}
