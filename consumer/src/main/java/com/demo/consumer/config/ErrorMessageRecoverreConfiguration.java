package com.demo.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry",name = "enabled",havingValue = "true")
public class ErrorMessageRecoverreConfiguration {
    @Bean
    public DirectExchange errExchange() {
        return new DirectExchange("err.direct");
    }

    @Bean
    public Queue errQueue() {
        return new Queue("err.queue");
    }

    @Bean
    public Binding errBinding() {
        return BindingBuilder.bind(errQueue()).to(errExchange()).with("err.direct");
    }

    @Bean
    public MessageRecoverer errMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate,"err.direct","err.direct");
    }
}
