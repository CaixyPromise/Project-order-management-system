package com.caixy.adminSystem.utils;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

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
        // 构建消息
        MessagePostProcessor messagePostProcessor = buildMessagePostProcessor(queueEnum.getDelayTime());
        CorrelationData correlationData = buildCorrelationData();
        // 发送消息
        rabbitTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRoutingKey(), message,
                messagePostProcessor, correlationData);
        log.info("发送延迟消息，消息ID为：{}", correlationData.getId());

    }

    /**
     * 构建延迟消息的 MessagePostProcessor
     *
     * @param delay 延迟时间，单位为毫秒
     * @return MessagePostProcessor
     */
    private MessagePostProcessor buildMessagePostProcessor(long delay)
    {
        return msg -> {
            msg.getMessageProperties().setHeader("x-delay", delay);
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

    private CorrelationData buildCorrelationData() {
        String messageId = UUID.randomUUID().toString();
        return new CorrelationData(messageId);
    }

}
