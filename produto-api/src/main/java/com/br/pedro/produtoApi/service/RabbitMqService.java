package com.br.pedro.produtoApi.service;

import com.br.pedro.produtoApi.dto.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String exchange, String routingKey, ProductDTO productDTO) throws JsonProcessingException {
        var jsonProduct = objectMapper.writeValueAsString(productDTO);
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonProduct, message -> {
            message.getMessageProperties().setHeader("EVENT","PRODUCT_CHANGED");
            return message;
        });
    }
}
