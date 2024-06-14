package com.caixy.adminSystem.model.enums;

import com.caixy.adminSystem.constant.RocketConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;

/**
 * 延迟队列信息枚举封装
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.enums.RocketDelayQueueEnum
 * @since 2024-06-14 16:27
 **/
@Getter
@AllArgsConstructor
public enum RocketDelayQueueEnum
{
    ORDER_ATTACHMENT(RocketConstant.ORDER_ATTACHMENT_TOPIC,
            "order_attachment",
            RocketDelayLevel.TEN_MINUTES,
            10000);

    /**
     * 延迟队列名称
     */
    private final String topic;

    /**
     * 延迟队列分组
     */
    private final String group;

    /**
     * 延迟级别
     */
    private final RocketDelayLevel delayLevel;

    /**
     * 超时时间
     */
    private final Integer timeout;

    /**
     * 重写 getDelayLevel 方法，获取延迟级别
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/14 下午4:58
     */
    public Integer getDelayLevel()
    {
        return delayLevel.getLevel();
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    @Nullable
    public static RocketDelayQueueEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (RocketDelayQueueEnum anEnum : RocketDelayQueueEnum.values())
        {
            if (anEnum.group.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }
}
