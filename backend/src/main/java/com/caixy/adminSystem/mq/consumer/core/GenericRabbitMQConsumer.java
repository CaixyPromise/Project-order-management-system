package com.caixy.adminSystem.mq.consumer.core;

import com.caixy.adminSystem.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 通用的消息消费类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.core.GenericRabbitMQConsumer
 * @since 2024-06-20 13:15
 **/
public abstract class GenericRabbitMQConsumer<T> implements RabbitMQMessageHandler<T>, ChannelAwareMessageListener
{
    private final Class<T> messageType;

    @SuppressWarnings("unchecked")
    public GenericRabbitMQConsumer() {
        // 通过反射获取泛型的实际类型
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) superClass).getActualTypeArguments();
            this.messageType = (Class<T>) actualTypeArguments[0];
        } else {
            throw new IllegalArgumentException("Class is not parameterized with generic type.");
        }
    }

    // 处理正常队列的消息
    @Override
    public void onMessage(Message rawMessage, Channel channel) throws Exception
    {
        T message = JsonUtils.byteArrayToJson(rawMessage.getBody(), StandardCharsets.UTF_8, messageType);
        try
        {
            handleMessage(message, channel, rawMessage);
        }
        catch (Exception e)
        {
            // 处理失败逻辑
            rejectMessage(channel, rawMessage.getMessageProperties().getDeliveryTag(), false);
        }
    }

    // 处理死信队列的消息
    @Override
    public void handleDeadLetterMessage(T message, Channel channel, Message rawMessage) throws Exception
    {
        // 由子类实现
    }

    protected void confirmMessage(Channel channel, long deliveryTag) throws IOException
    {
        channel.basicAck(deliveryTag, false);
    }

    protected void rejectMessage(Channel channel, long deliveryTag, boolean requeue) throws IOException
    {
        channel.basicNack(deliveryTag, false, requeue);
    }
}