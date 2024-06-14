package com.caixy.adminSystem.utils;

import com.caixy.adminSystem.model.enums.RocketDelayQueueEnum;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消息队列工具类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.utils.RocketMqUtils
 * @since 2024-06-14 16:49
 **/
@Component
@AllArgsConstructor
public class RocketMqUtils
{
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 构建消息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/14 下午4:51
     */
    private Message<?> buildMessage(Object message)
    {
        return MessageBuilder.withPayload(message).build();
    }

    /**
     * 发送延迟消息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/14 下午4:54
     */
    public SendResult sendDelayMessage(RocketDelayQueueEnum queueEnum, Object message)
    {
        return rocketMQTemplate.syncSend(queueEnum.getTopic(),
                buildMessage(message),
                queueEnum.getTimeout(),
                queueEnum.getDelayLevel()
        );
    }

}
