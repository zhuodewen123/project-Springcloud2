package com.zhuodewen.www.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface RabbitMqService {

    /**
     * rabbitMq接口--消息发送者
     * @return
     */
    @Output("rabbit")
    SubscribableChannel sendMessage();
}
