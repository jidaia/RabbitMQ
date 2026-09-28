package com.demo.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DirectConfiguration {
    @Bean
    public DirectExchange directExchange() {
        //方法1通过 ExchangeBuilder声明
//        Exchange build = ExchangeBuilder.fanoutExchange("pomian.faout").durable(true).build();
//        return (FanoutExchange) build;

        //方法2 直接new
        return new DirectExchange("pomian.fanout2");
    }

    @Bean
    public Queue directQueue1() {
        //方法1 通过qQueueBuilder 建立queue
//        QueueBuilder.durable("pomian.fanout").build();
        //法二直接new
        return new Queue("direct.queue1");
    }





}
