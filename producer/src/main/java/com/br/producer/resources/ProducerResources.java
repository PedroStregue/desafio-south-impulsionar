package com.br.producer.resources;

import com.br.producer.entity.Product;
import com.br.producer.services.ProducerServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerResources {
    @Autowired
    ProducerServices services;
    @PostMapping
    public void save(@RequestBody Product product){
        try {
            services.save(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
