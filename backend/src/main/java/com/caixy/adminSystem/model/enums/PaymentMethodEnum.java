package com.caixy.adminSystem.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @name: com.caixy.adminSystem.model.enums.PaymentMethodEnum
 * @description: 支付方法枚举
 * @author: CAIXYPROMISE
 * @date: 2024-06-06 14:13
 **/
@Getter
public enum PaymentMethodEnum
{

    /**
     * 支付宝支付
     */
    ALIPAY(1, "支付宝支付"),

    /**
     * 微信支付
     */
    WECHATPAY(2, "微信支付"),

    /**
     * 银联支付
     */
    UNIONPAY(3, "银联支付"),

    /**
     * 现金支付
     */
    CASHPAY(4, "现金支付"),

    /**
     * 刷卡支付
     */
    SWIPEPAY(5, "刷卡支付");

    private final Integer code;

    private final String desc;

    PaymentMethodEnum(Integer code, String desc)
    {

        this.code = code;
        this.desc = desc;
    }

    public static PaymentMethodEnum getByCode(Integer code)
    {

        for (PaymentMethodEnum paymentMethodEnum : values())
        {
            if (paymentMethodEnum.getCode().equals(code))
            {
                return paymentMethodEnum;
            }
        }
        return null;
    }
}
