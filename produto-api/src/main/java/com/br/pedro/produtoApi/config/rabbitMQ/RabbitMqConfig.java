package com.br.pedro.produtoApi.config.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitMqConfig {

    private static final String QUEUE_NAME = "ProductQueue";
    private static final String EXCHANGE_NAME  = "amq.direct";
    @Autowired
    private AmqpAdmin amqpAdmin;




   private DirectExchange exchangeDirect(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    private Queue queueProduct(String queueName){
       return new Queue(queueName,true,false,false);
    }

    private Binding binding(Queue queue,DirectExchange exchange){
       return new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchangeDirect().getName(), queue.getName(),null);
    }
    @PostConstruct
    private void addQueue(){
       Queue productQueue = this.queueProduct(QUEUE_NAME);

       DirectExchange exchange = this.exchangeDirect();

       Binding binding = this.binding(productQueue, exchange);

       this.amqpAdmin.declareQueue(productQueue);
       this.amqpAdmin.declareExchange(exchange);
       this.amqpAdmin.declareBinding(binding);
    }
}
