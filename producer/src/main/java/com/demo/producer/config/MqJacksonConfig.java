package com.demo.producer.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqJacksonConfig {
    //Jackson 3 JSON序列化，替换默认JDK序列化（Jackson2已弃用）
    @Bean
    public MessageConverter jacksonMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        //开启消息唯一ID 内部原理是 UUID
        converter.setCreateMessageIds(true);
        return converter;
    }
}
