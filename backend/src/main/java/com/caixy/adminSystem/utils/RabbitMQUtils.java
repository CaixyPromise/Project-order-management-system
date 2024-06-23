package com.caixy.adminSystem.utils;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * RocketMQ消息队列工具类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.utils.RabbitMQUtils
 * @since 2024-06-14 16:49
 **/
@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQUtils
{
    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送延迟消息
     *
     * @param queueEnum 队列枚举信息
     * @param message   要发送的消息内容
     */
    public void sendDelayedMessage(RabbitMQQueueEnum queueEnum, Object message)
    {
        if (queueEnum.getDelayTime() == null)
        {
            throw new RuntimeException("延迟时间不能为空");
        }
        // 构建消息属性映射
        Map<String, Object> messageProperties = Map.of(
                "x-delay", queueEnum.getDelayTime()
        );

        // 构建消息
        MessagePostProcessor messagePostProcessor = buildMessagePostProcessor(messageProperties);
        CorrelationData correlationData = buildCorrelationData();
        // 发送消息
        rabbitTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRoutingKey(), message,
                messagePostProcessor, correlationData);
        log.info("发送延迟消息，消息ID为：{}", correlationData.getId());

    }

    /**
     * 发送延迟消息，并保持重试计数
     *
     * @param message       消息内容
     * @param retryCount 当前重试次数
     */
    public void sendDelayedMessageWithRetry(RabbitMQQueueEnum queueEnum, Object message, int retryCount)
    {
        // 构建消息属性映射
        Map<String, Object> messageProperties = Map.of(
                "x-delay", queueEnum.getDelayTime(),
                "x-retry-count", retryCount
        );

        // 构建消息处理器，将这些属性应用到消息上
        MessagePostProcessor messagePostProcessor = buildMessagePostProcessor(messageProperties);

        // 生成消息唯一标识
        CorrelationData correlationData = buildCorrelationData();
        // 设置消息的内容类型为 application/json
        MessagePostProcessor jsonMessagePostProcessor = msg -> {
            msg.getMessageProperties().setContentType("application/json");
            return messagePostProcessor.postProcessMessage(msg);
        };
        // 发送消息
        rabbitTemplate.convertAndSend(queueEnum.getExchange(),
                queueEnum.getRoutingKey(),
                message,
                jsonMessagePostProcessor,
                correlationData);

        log.info("发送延迟消息，消息ID为：{}，重试次数为：{}", correlationData.getId(), retryCount);
    }

    /**
     * 构建延迟消息的 MessagePostProcessor
     *
     * @param properties 要设置的消息属性映射
     * @return MessagePostProcessor
     */
    private MessagePostProcessor buildMessagePostProcessor(Map<String, Object> properties)
    {
        return msg -> {
            properties.forEach((key, value) -> msg.getMessageProperties().setHeader(key, value));
            return msg;
        };
    }

    /**
     * 发送非延迟消息
     *
     * @param queueEnum 队列枚举信息
     * @param message   要发送的消息内容
     */
    public void sendMessage(RabbitMQQueueEnum queueEnum, Object message)
    {
        CorrelationData correlationData = buildCorrelationData();
        // 直接发送消息
        rabbitTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRoutingKey(), message, correlationData);
        log.info("发送消息，消息ID为：{}", correlationData.getId());
    }

    private CorrelationData buildCorrelationData()
    {
        String messageId = UUID.randomUUID().toString();
        return new CorrelationData(messageId);
    }

}
