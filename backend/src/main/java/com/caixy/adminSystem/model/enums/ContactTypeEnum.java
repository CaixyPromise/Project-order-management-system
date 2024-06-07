package com.caixy.adminSystem.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 联系方式类型枚举
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.enums.ContactTypeEnum
 * @since 2024-06-06 12:34
 **/
@Getter
public enum ContactTypeEnum
{

    /**
     * 闲鱼
     */
    XIANYU(0, "闲鱼"),

    /**
     * 邮箱
     */
    EMAIL(1, "邮箱"),
    /**
     * 微信
     */
    WECHAT(2, "微信"),
    /**
     * QQ
     */
    QQ(3, "QQ"),
    /**
     * 钉钉
     */
    DINGTALK(4, "钉钉"),
    /**
     * 企业微信
     */
    WORK_WEIXIN(5, "企业微信"),

    /**
     * 手机
     */
    PHONE(6, "手机"),
    ;

    private final Integer value;

    private final String text;

    ContactTypeEnum(Integer value, String text)
    {

        this.value = value;
        this.text = text;

    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues()
    {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ContactTypeEnum getEnumByValue(Integer value)
    {
        if (value == null)
        {
            return null;
        }
        for (ContactTypeEnum anEnum : ContactTypeEnum.values())
        {
            if (anEnum.value.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }
}
