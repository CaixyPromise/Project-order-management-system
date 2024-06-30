package com.caixy.adminSystem.config.properties.RabbitMQ.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交换机类型枚举
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.properties.RabbitMQ.core.ExchangeTypeEnum
 * @since 2024-06-29 16:40
 **/
@Getter
@AllArgsConstructor
public enum ExchangeTypeEnum
{

    DIRECT("direct", "定向交换机"),

    FANOUT("fanout", "扇形交换机"),

    TOPIC("topic", "主题交换机"),

    HEADERS("headers", "头交换机"),

    DELAY("x-delayed-message", "延迟交换机");

    private final String value;
    private final String label;

    public static ExchangeTypeEnum getEnumByValue(String value)
    {
        if (value == null)
        {
            return null;
        }
        for (ExchangeTypeEnum typeEnum : ExchangeTypeEnum.values())
        {
            if (typeEnum.getValue().equals(value))
            {
                return typeEnum;
            }
        }
        return null;
    }
}
