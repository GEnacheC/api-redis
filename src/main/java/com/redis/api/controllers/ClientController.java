package com.redis.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis.api.models.Client;
import com.redis.api.services.ClientService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    
    @Autowired
    ClientService service;

    @GetMapping
    public ResponseEntity<List<Client>> get() {
        return ResponseEntity.ok().body(service.getAll());
    }
    
}
