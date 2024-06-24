package com.caixy.adminSystem.model.enums;

import lombok.Getter;

/**
 * 订单提醒紧急状态枚举
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.enums.UrgencyLevelEnum
 * @since 2024-06-24 13:00
 **/
@Getter
public enum UrgencyLevelEnum
{
    HIGH("error", "高紧急度 - 3天内截止"),
    MEDIUM("warning", "中等紧急度 - 一周内截止"),
    LOW("processing", "低紧急度 - 超过一周截止");

    private final String level;
    private final String description;

    UrgencyLevelEnum(String level, String description)
    {
        this.level = level;
        this.description = description;
    }

    public static UrgencyLevelEnum getUrgency(long daysUntilDeadline)
    {
        if (daysUntilDeadline <= 3)
        {
            return HIGH;
        }
        else if (daysUntilDeadline <= 7)
        {
            return MEDIUM;
        }
        else
        {
            return LOW;
        }
    }
}
