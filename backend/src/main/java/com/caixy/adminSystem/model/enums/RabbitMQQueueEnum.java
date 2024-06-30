package com.caixy.adminSystem.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;

/**
 * @name: com.caixy.adminSystem.model.enums.RabbitMQQueueEnum
 * @description: 普通消息队列信息枚举封装
 * @author: CAIXYPROMISE
 * @date: 2024-06-19 22:30
 **/
@AllArgsConstructor
@Getter
public enum RabbitMQQueueEnum
{
    /**
     * 订单附件
     */
    ORDER_ATTACHMENT("orderDelayExchange",
            "order.attachment",
            "orderDelayAttachmentQueue",
            "X-DeadLetter-Attachment-Queue",
            // 延迟时间，单位为毫秒, 10 分钟
            60L * 1000L * 10L,
            true),
    /**
     * 订单更新
     */
    ORDER_ASYNC_SAVE_ES("orderInfoExchange",
            "order.save",
            "orderSaveQueue",
            null,
            null,
            false
    ),
    ;
    /**
     * 交换机名称
     */
    private final String exchange;

    /**
     * 路由键
     */
    private final String routingKey;

    /**
     * 队列名称
     */
    private final String queueName;

    /**
     * 死信队列名称
     */
    private final String deadLetterQueue;

    /**
     * 延迟时间
     */
    private final Long delayTime;

    /**
     * 是否是延迟队列
     */
    private final Boolean isDelay;



    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    @Nullable
    public static RabbitMQQueueEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (RabbitMQQueueEnum anEnum : RabbitMQQueueEnum.values())
        {
            if (anEnum.routingKey.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }
}
