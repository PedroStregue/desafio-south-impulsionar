package com.br.pedro.produtoApi.config.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

   @Bean
   public DirectExchange exchangeDirect(){
        return ExchangeBuilder.directExchange("ProductExchange").durable(true).build();
    }
    @Bean
    public Queue queueProduct(){
       return QueueBuilder.durable("ProductQueue").build();
    }

    public Binding binding(Queue queue,DirectExchange exchange){
       return BindingBuilder.bind(queue).to(exchange).with("ProductQueue");
    }
}
