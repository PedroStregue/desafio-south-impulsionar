package com.br.pedro.produtoApi.consumer;

import com.br.pedro.produtoApi.config.rabbitMQ.RabbitMqConfig;
import com.br.pedro.produtoApi.convert.ProductConvert;
import com.br.pedro.produtoApi.dto.ProductDTO;
import com.br.pedro.produtoApi.entity.Product;
import com.br.pedro.produtoApi.repository.ProductRepository;
import com.br.pedro.produtoApi.service.ProductServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Slf4j
@Component
public class ProductConsumer {
    @Autowired
    ProductRepository rep;
    @Autowired
    ProductServices services;

    @Autowired
    ObjectMapper objectMapper;
    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer(Message<String> message) throws JsonProcessingException {
        Product product = objectMapper.readValue(message.getPayload(), Product.class);

        var messageHeader = message.getHeaders().get("EVENT");
        var productBody = message.getPayload();
//        log.info(messageHeader.toString());
//        log.info(productBody);
        switch(messageHeader.toString()){
            case "PRODUCT_CHANGED":
                rep.save(product);
                break;
            case "CREATE":
                rep.save(product);
                break;
            case "UPDATE":
                services.update(product.getId(), product);
                break;
        }

    }
}
