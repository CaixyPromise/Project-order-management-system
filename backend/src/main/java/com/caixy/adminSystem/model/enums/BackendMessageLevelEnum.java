package com.caixy.adminSystem.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 站内通知消息等级
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.enums.BackendMessageLevelEnum
 * @since 2024-06-14 21:11
 **/
@Getter
@AllArgsConstructor
public enum BackendMessageLevelEnum
{
    NORMAL(1, "普通"),
    MENTION(3, "提醒"),
    WARNING(2, "警告"),
    ERROR(4, "错误"),
    ;
    private final Integer code;
    private final String text;
    public static BackendMessageLevelEnum getByCode(Integer code)
    {

        for (BackendMessageLevelEnum paymentMethodEnum : values())
        {
            if (paymentMethodEnum.getCode().equals(code))
            {
                return paymentMethodEnum;
            }
        }
        return null;
    }
}
