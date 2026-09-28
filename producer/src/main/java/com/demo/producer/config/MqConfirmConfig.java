package com.demo.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MqConfirmConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitmq = applicationContext.getBean(RabbitTemplate.class);
        //配置ConfirmCallback：Broker确认消息是否到达交换机
        rabbitmq.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.info("消息确认到达交换机, id={}", correlationData.getId());
                } else {
                    log.error("消息被拒收, id={}, 原因: {}", correlationData.getId(), cause);
                }
            }
        });
    }
}
