package com.caixy.adminSystem.config;

import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * GsonRedis构造器
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.GsonRedisSerializer
 * @since 2024-06-16 11:07
 **/
public class GsonRedisSerializer<T> implements RedisSerializer<T>
{
    private final Gson gson;
    private final Class<T> type;

    public GsonRedisSerializer(Class<T> type)
    {
        this.gson = new Gson();
        this.type = type;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return gson.toJson(t).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        String json = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(json, type);
    }
}