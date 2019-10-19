package com.zhuodewen.www.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RabbitMqService {

    /**
     * rabbitMq接口--消费者
     * @return
     */
    @Input("rabbit")
    SubscribableChannel receiveMessage();
}
