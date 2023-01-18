package com.br.producer.services;

import com.br.producer.dto.ProductDTO;
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

    public void sendMessage(String exchange, String routingKey, ProductDTO productDTO, String header) throws JsonProcessingException {
        var productJson = objectMapper.writeValueAsString(productDTO);

        rabbitTemplate.convertAndSend(exchange,routingKey,productJson, message -> {
            message.getMessageProperties().setHeader("EVENT",header);
            return message;
        });

    }
}
