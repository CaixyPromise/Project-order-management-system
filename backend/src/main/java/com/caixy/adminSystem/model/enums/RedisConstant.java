package com.caixy.adminSystem.model.enums;

import lombok.Getter;

@Getter
public enum RedisConstant
{
    CATEGORY_PARENT_BY_KEY("category:parent:", 3600L * 24 * 7),
    // 上传文件key, 过期时间10分钟
    UPLOAD_FILE_KEY("upload:file:", 60L * 10),
    /**
     * 下载文件key, 过期时间10分钟
     */
    DOWNLOAD_FILE_KEY("download:file:", 60L * 10),
    /**
     * 语言类型选项缓存，过期时间7天
     */
    LANGUAGE_TYPE("language:", 3600L * 24 * 7),
    /**
     * 订单分类选项缓存，过期时间7天
     */
    ORDER_CATEGORY("order:", 3600L * 24 * 7),




    ;
    private final String key;
    private final Long expire;

    RedisConstant(String key, Long expire)
    {
        this.key = key;
        this.expire = expire;
    }

    public String generateKey(String... values)
    {
        String joinValue = String.join(":", values);
        if (this.key.charAt(this.key.length() - 1) == ':')
        {
            return this.key + joinValue;
        }
        return this.key + ":" + joinValue;
    }
}
