package com.caixy.adminSystem.model.enums;

import lombok.Getter;

/**
 * @name: com.caixy.adminSystem.model.enums.RocketDelayLevel
 * @description: RocketMQ 的延迟级别
 * @author: CAIXYPROMISE
 * @date: 2024-06-14 16:56
 **/
@Getter
public enum RocketDelayLevel
{
    ONE_SECOND(1, "1 秒"),
    FIVE_SECONDS(2, "5 秒"),
    TEN_SECONDS(3, "10 秒"),
    THIRTY_SECONDS(4, "30 秒"),
    ONE_MINUTE(5, "1 分钟"),
    TWO_MINUTES(6, "2 分钟"),
    THREE_MINUTES(7, "3 分钟"),
    FOUR_MINUTES(8, "4 分钟"),
    FIVE_MINUTES(9, "5 分钟"),
    SIX_MINUTES(10, "6 分钟"),
    SEVEN_MINUTES(11, "7 分钟"),
    EIGHT_MINUTES(12, "8 分钟"),
    NINE_MINUTES(13, "9 分钟"),
    TEN_MINUTES(14, "10 分钟"),
    TWENTY_MINUTES(15, "20 分钟"),
    THIRTY_MINUTES(16, "30 分钟"),
    ONE_HOUR(17, "1 小时"),
    TWO_HOURS(18, "2 小时");

    private final int level;
    private final String description;

    RocketDelayLevel(int level, String description)
    {
        this.level = level;
        this.description = description;
    }

    public static RocketDelayLevel fromLevel(int level)
    {
        for (RocketDelayLevel delayLevel : values())
        {
            if (delayLevel.getLevel() == level)
            {
                return delayLevel;
            }
        }
        throw new IllegalArgumentException("Invalid delay level: " + level);
    }
}
