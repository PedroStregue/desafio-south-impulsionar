package com.br.producer.resources;

import com.br.producer.entity.Product;
import com.br.producer.services.ProducerServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer")
public class ProducerResources {
    @Autowired
    ProducerServices services;
    @PostMapping
    public ResponseEntity save(@RequestBody Product product){
        try {
            return ResponseEntity.ok(services.save(product));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Product product){
        try {
            services.update(product);
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
