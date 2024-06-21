package com.caixy.adminSystem.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 锁操作工具类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.utils.LockUtil
 * @since 2024-06-17 21:21
 **/
public class LockUtil
{
    @FunctionalInterface
    public interface LockExecutor<T>
    {
        T execute() throws InterruptedException;
    }

    public static <T> T executeWithLock(Lock lock, long timeout, TimeUnit unit, LockExecutor<T> executor)
    {
        boolean locked = false;
        try
        {
            locked = lock.tryLock(timeout, unit); // 尝试获取锁，等待指定的时间
            if (!locked)
            {
                throw new RuntimeException("获取锁超时");
            }
            return executor.execute(); // 执行受保护的代码块
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException("操作中断", e);
        }
        finally
        {
            if (locked)
            {
                lock.unlock(); // 释放锁
            }
        }
    }
}
