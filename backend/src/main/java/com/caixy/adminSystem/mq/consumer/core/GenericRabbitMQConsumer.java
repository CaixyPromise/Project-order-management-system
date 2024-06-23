package com.caixy.adminSystem.mq.consumer.core;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.utils.JsonUtils;
import com.caixy.adminSystem.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import javax.annotation.Resource;
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
@Slf4j
public abstract class GenericRabbitMQConsumer<T> implements RabbitMQMessageHandler<T>, ChannelAwareMessageListener
{
    @Resource
    protected RabbitMQUtils rabbitMQUtils;

    private final Class<T> messageType;

    private static final String RETRY_COUNT_KEY = "x-retry-count";

    @SuppressWarnings("unchecked")
    public GenericRabbitMQConsumer()
    {
        // 通过反射获取泛型的实际类型
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType)
        {
            Type[] actualTypeArguments = ((ParameterizedType) superClass).getActualTypeArguments();
            this.messageType = (Class<T>) actualTypeArguments[0];
        }
        else
        {
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

    /**
     * 丢弃消息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:10
     */
    protected void discardMessage(Channel channel, long deliveryTag) throws IOException
    {
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 拒绝消息，并重新入队
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:10
     */
    protected void rejectMessage(Channel channel, long deliveryTag, boolean requeue) throws IOException
    {
        channel.basicNack(deliveryTag, false, requeue);
    }

    /**
     * 拒绝消息，并放入死信队列
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/23 下午12:09
     */
    protected void rejectMessage(Channel channel, long deliveryTag) throws IOException
    {
        channel.basicReject(deliveryTag, false);
    }

    /**
     * 拒绝消息，并根据最大重试次数进行重试或丢弃
     *
     * @param channel    RabbitMQ 通道对象
     * @param rawMessage 原始消息对象
     * @param maxRetries 最大重试次数
     */
    protected void rejectAndRetryOrDiscard(Channel channel,
                                           Message rawMessage,
                                           Object message,
                                           RabbitMQQueueEnum rabbitMQQueueEnum,
                                           int maxRetries) throws IOException
    {
        long deliveryTag = rawMessage.getMessageProperties().getDeliveryTag();
        Integer retryCount = getRetryCount(rawMessage);

        if (retryCount < maxRetries)
        {
            // 增加重试次数并重新发送消息到延迟队列
            int newRetryCount = retryCount + 1;
            rabbitMQUtils.sendDelayedMessageWithRetry(rabbitMQQueueEnum,  message, newRetryCount);
            // 丢弃原始消息，源消息
            discardMessage(channel, deliveryTag);
        }
        else
        {
            log.info("达到最大重试次数，拒绝消息，不重新入队（消息会进入死信队列或被丢弃）");
            // 达到最大重试次数，拒绝消息，不重新入队（消息会进入死信队列或被丢弃）
            rejectMessage(channel, deliveryTag);
        }
    }


    protected Integer getRetryCount(Message rawMessage)
    {
        return (Integer) rawMessage.getMessageProperties().getHeaders().getOrDefault(RETRY_COUNT_KEY, 0);
    }
}