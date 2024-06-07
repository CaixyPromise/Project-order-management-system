package com.caixy.adminSystem.model.enums;

import lombok.Getter;

/**
 * @name: com.caixy.adminSystem.model.enums.OrderSourceEnum
 * @description: 订单来源枚举类
 * @author: CAIXYPROMISE
 * @date: 2024-06-06 14:17
 **/
@Getter
public enum OrderSourceEnum
{
    XIAN_YU(1, "闲鱼"),

    WX_GROUP(2, "微信群"),

    WX_FRIEND(3, "微信好友"),

    QQ_GROUP(4, "QQ群"),

    QQ_FRIEND(5, "QQ好友"),
    ;

    private final Integer code;

    private final String desc;

    OrderSourceEnum(Integer code, String desc)
    {

        this.code = code;
        this.desc = desc;
    }

    public static OrderSourceEnum getByCode(Integer code)
    {

        for (OrderSourceEnum paymentMethodEnum : values())
        {
            if (paymentMethodEnum.getCode().equals(code))
            {
                return paymentMethodEnum;
            }
        }
        return null;
    }
}
