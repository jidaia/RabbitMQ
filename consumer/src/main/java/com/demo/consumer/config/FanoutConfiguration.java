package com.demo.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {
    @Bean
    public FanoutExchange fanoutExchange() {
        //方法1通过 ExchangeBuilder声明
//        Exchange build = ExchangeBuilder.fanoutExchange("pomian.faout").durable(true).build();
//        return (FanoutExchange) build;

        //方法2 直接new
        return new FanoutExchange("pomian.fanout2");
    }

    @Bean
    public Queue fanoutQueue3() {
        //方法1 通过qQueueBuilder 建立queue
//        QueueBuilder.durable("pomian.fanout").build();
        //法二直接new
        return new Queue("fanout.queue3");
    }

    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }


    @Bean
    public Queue fanoutQueue4() {
        return new Queue("fanout.queue4");
    }

    @Bean
    public Binding FanoutBinding4() {
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());
    }
}
