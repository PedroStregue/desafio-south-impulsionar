package com.br.producer.config.rabbitMQ;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "ProductQueue";
    public static final String EXCHANGE_NAME  = "amq.fanout";
    @Autowired
    private AmqpAdmin amqpAdmin;

   @Bean
   public FanoutExchange exchange(){
        return new FanoutExchange(EXCHANGE_NAME);
    }
    @Bean
    public Queue queueProduct(){
       return new Queue(QUEUE_NAME,true,false,false);
    }
    @Bean
    public Binding binding(Queue queue,FanoutExchange exchange){
       return new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchange().getName(), queue.getName(),null);
    }
//    @PostConstruct
//    private void addQueue(){
//       Queue productQueue = this.queueProduct(QUEUE_NAME);
//
//       FanoutExchange exchange = this.exchange();
//
//       Binding binding = this.binding(productQueue, exchange);
//
//       this.amqpAdmin.declareQueue(productQueue);
//       this.amqpAdmin.declareExchange(exchange);
//       this.amqpAdmin.declareBinding(binding);
//    }
}
