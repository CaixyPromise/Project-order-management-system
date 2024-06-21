package com.caixy.adminSystem.mq.producer.core;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.utils.RabbitMQUtils;

import javax.annotation.Resource;

/**
 * 通用生产者方法类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.producer.core.GenericRabbitMQProducer
 * @since 2024-06-20 15:15
 **/

public abstract class GenericRabbitMQProducer<T> implements RabbitMQProducerHandler<T>
{
    @Resource
    protected RabbitMQUtils rabbitMQUtils;

    // 默认的发送消息方法可以在这里实现，子类可以覆盖这个方法
    @Override
    public void sendMessage(RabbitMQQueueEnum queueEnum, T message)
    {
        rabbitMQUtils.sendMessage(queueEnum, message);
    }
}
