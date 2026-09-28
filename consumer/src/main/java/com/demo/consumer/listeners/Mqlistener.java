package com.demo.consumer.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
public class Mqlistener {
    //一个队列对一个消费者
    @RabbitListener(queues = "queue1")
    public void listenerQueue1(String msg){
        System.out.println("消费者收到了queue1的消息:"+msg);
        throw  new RuntimeException("故意的"); //uack异常
//        throw new MessageConversionException("故意的");//reject异常
    }

    //一个队列对多个消费者(work queue模型)
    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1 收到了work.queue1的消息:"+msg);
        Thread.sleep(2);
    }

    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2 收到了work.queue2的消息:"+msg);
        Thread.sleep(20);
    }

    //通过fanout交换机实现广播
    @RabbitListener(queues = "fanout.queue1")
    public void listenerFanoutQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1 收到了fanout.queue1的消息:"+msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenerFanoutqueue2(String msg) throws InterruptedException {
        System.out.println("消费者2 收到了fanout.queue2的消息:"+msg);
    }

    //通过direct交换机实现定向路由
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1",durable = "true"),
            exchange =@Exchange(name = "pomian.direct",type = ExchangeTypes.DIRECT),
            key = {"red","blue"}
    ))
    public void listenerDirectQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1 收到了direct.queue1的消息:"+msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2",durable = "true"),
            exchange =@Exchange(name = "pomian.direct",type = ExchangeTypes.DIRECT),
            key = {"red","yellow"}
    ))
    public void listenerDirectQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2 收到了direct.queue2的消息:"+msg);
    }

    //通过topic交换机实现模糊匹配
    @RabbitListener(queues = "topic.queue1")
    public void listenerTopicQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1 收到了topic.queue1的消息:"+msg);
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenerTopicQueue2(String msg) throws InterruptedException {
        System.out.println("消费者2 收到了topic.queue2的消息:"+msg);
    }

    @RabbitListener(queues = "object.queue")
    public void listenerObjectQueue(Map<String, Object> msg) throws InterruptedException {
        System.out.println("消费者 收到了object.queue的消息:"+msg);
    }

    @RabbitListener(queues = "dlx.queue")
    public void listenerDlxQueue(String msg) throws InterruptedException {
        log.info("消费者1 收到了dlx.queue的消息:"+msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "delay.queue",
                    durable = "true",
                    arguments = {
                            @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"), // 消息10秒过期
                            @Argument(name = "x-dead-letter-exchange", value = "pomian.dead"),            // 绑定死信交换机
                            @Argument(name = "x-dead-letter-routing-key", value = "dead.queue")            // 死信路由键
                    }
            ),
            exchange = @Exchange(name = "pomian.normal", type = ExchangeTypes.DIRECT),
            key = "delay.queue"
    ))
    public void listenerDelay(String msg) {
        System.out.println("收到延迟消息: " + msg);
    }


}
