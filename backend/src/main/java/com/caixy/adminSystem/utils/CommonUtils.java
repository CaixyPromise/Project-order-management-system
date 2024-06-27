package com.caixy.adminSystem.utils;

import java.util.Optional;
import java.util.function.Function;

/**
 * 通用工具类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.utils.CommonUtils
 * @since 2024-06-25 17:33
 **/
public class CommonUtils
{
    public static  <T, R> R isPresent(T target, R defaultValue, Function<T, R> mapper)
    {
        return Optional.ofNullable(target).map(mapper).orElse(defaultValue);
    }

    public static <T> T isPresent(T target, T defaultValue)
    {
        return Optional.ofNullable(target).orElse(defaultValue);
    }

    public static boolean integerToBool(Integer value)
    {
        return value != null && (value == 0 || value == 1);
    }
}
