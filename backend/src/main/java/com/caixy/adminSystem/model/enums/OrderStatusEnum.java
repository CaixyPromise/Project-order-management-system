package com.caixy.adminSystem.model.enums;

import lombok.Getter;

/**
 * @name: com.caixy.adminSystem.model.enums.OrderStatusEnum
 * @description: 订单状态枚举类
 * @author: CAIXYPROMISE
 * @date: 2024-06-06 14:19
 **/
@Getter
public enum OrderStatusEnum
{
    /**
     * 订单状态枚举类
     */
    PAYING(1, "待支付"),
    PAYED(2, "已支付-正在进行"),
    REFUNDED(3, "已退款"),
    CANCELED(4, "已取消"),
    FINISHED(5, "已完成"),

    ;
    private final Integer code;

    private final String desc;

    OrderStatusEnum(Integer code, String desc)
    {

        this.code = code;
        this.desc = desc;
    }

    public static OrderStatusEnum getByCode(Integer code)
    {

        for (OrderStatusEnum paymentMethodEnum : values())
        {
            if (paymentMethodEnum.getCode().equals(code))
            {
                return paymentMethodEnum;
            }
        }
        return null;
    }
}
