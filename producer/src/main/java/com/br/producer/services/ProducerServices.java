package com.br.producer.services;

import com.br.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProducerServices {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;
    public Product save(Product product) throws JsonProcessingException {
        var productJson = objectMapper.writeValueAsString(product);
        rabbitTemplate.convertAndSend("amq.fanout","ProductQueue",productJson,message -> {
            message.getMessageProperties().setHeader("EVENT","CREATE");
            return message;
        });
        return product;
    }

    public void update(Product product) throws JsonProcessingException {
        var productJson = objectMapper.writeValueAsString(product);
        rabbitTemplate.convertAndSend("amq.fanout", "ProductQueue", productJson,message -> {
            message.getMessageProperties().setHeader("EVENT","UPDATE");
            return message;
        });
    }

//    public Product findById(long id){
//        rabbitTemplate.convertAndSend("amq.fanout","ProductQueue",id,message -> {
//            message.getMessageProperties().setHeader("EVENT","GET");
//            return message;
//        });
//    }
}
