package com.demo.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.batch.MessageBatch;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@SpringBootTest
public class SpringAMQPTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testSend() {
        String queueName = "queue1";
        String msg = "Hello Spring MQ10!";
        rabbitTemplate.convertAndSend(queueName,msg);
    }

    @Test
    public void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        for (int i = 1; i <=100 ; i++) {
            String msg = "Hello Spring MQ!"+i;
            rabbitTemplate.convertAndSend(queueName,msg);
        }
    }

    @Test
    public void testSendFanout() {
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        String exchangeName = "pomian.fanout";
        String msg = "hello fanout!";
        rabbitTemplate.convertAndSend(exchangeName,null,msg,cd);
    }

    @Test
    public void testSendDirect() {
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        String exchangeName = "pomian.direct";
        String msg = "hello direct!";
        rabbitTemplate.convertAndSend(exchangeName,"blue",msg,cd);
    }

    @Test
    public void testSendTopic() {
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        String exchangeName = "pomian.topic";
        String msg = "hello topic!";
        rabbitTemplate.convertAndSend(exchangeName,"china.weather",msg,cd);
    }

    @Test
    public void testSendObject() {
        //对象默认序列化
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        Map<String,Object> msg = new HashMap<>();
        msg.put("name","jack");
        msg.put("age",18);
        rabbitTemplate.convertAndSend("object.queue",msg,cd);
    }

    @Test
    public void testConfirmCallback() throws InterruptedException {
        String exchangeName = "pomian.direct";
        String msg = "hello direct!";
        String routingKey = "red";
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
  /*
        //添加ConfirmCallback（4.x 用 CompletableFuture 的 whenComplete，旧版 addCallback 已移除）
        cd.getFuture().whenComplete((confirm, throwable) -> {
            if (throwable != null) {
                System.err.println("发送异常: " + throwable.getMessage());
            } else if (confirm.ack()) {
                System.out.println("消息已确认到达交换机, id=" + cd.getId());
                //开启了publisher-returns后，若路由失败，这里能拿到退回的消息
                if (cd.getReturned() != null) {
                    System.err.println("路由失败! 退回消息的routingKey=" + cd.getReturned().getRoutingKey());
                }
            } else {
                System.err.println("消息被拒收: " + confirm.reason());
            }
        });*/

        rabbitTemplate.convertAndSend(exchangeName,routingKey,msg,cd);
        Thread.sleep(2000); //等待异步回调完成，再结束测试
    }

    @Test
    public void testMessageListener() throws InterruptedException {
        Message messg = MessageBuilder.withBody("hello".getBytes()).setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
        for(int i=0;i<1000000;i++){
            rabbitTemplate.convertAndSend("queue1",messg);
        }
    }

    @Test
    public void testTTLMessage() throws InterruptedException {
      rabbitTemplate.convertAndSend("simple.direct","hi","hello",new  MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException  {
                    message.getMessageProperties().setExpiration("1000");
                    return message;
                }
      });
        log.info("消息发送成功");
    }
}
