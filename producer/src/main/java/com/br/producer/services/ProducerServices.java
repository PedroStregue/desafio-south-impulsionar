package com.br.producer.services;

import com.br.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerServices {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;
    public void save(Product product) throws JsonProcessingException {
        var productJson = objectMapper.writeValueAsString(product);
        rabbitTemplate.convertAndSend("amq.direct","ProductQueue",productJson,message -> {
            message.getMessageProperties().setHeader("EVENT","CREATE");
            return message;
        });
    }
}
