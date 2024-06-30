package com.caixy.adminSystem.mq.consumer.core;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * 消息处理接口
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.core.RabbitMQMessageHandler
 * @since 2024-06-20 13:15
 **/
public interface RabbitMQMessageHandler<T>
{
    /**
     * 处理接收到的消息
     *
     * @param message    消息对象
     * @param channel    RabbitMQ的Channel对象
     * @param rawMessage 原始消息对象
     * @throws Exception 处理消息过程中可能抛出的异常
     */
    void handleMessage(T message, Channel channel, Message rawMessage, String messageId) throws Exception;

    /**
     * 处理死信队列中的消息
     *
     * @param message    消息对象
     * @param channel    RabbitMQ的Channel对象
     * @param rawMessage 原始消息对象
     * @throws Exception 处理消息过程中可能抛出的异常
     */
    void handleDeadLetterMessage(T message, Channel channel, Message rawMessage) throws Exception;
}