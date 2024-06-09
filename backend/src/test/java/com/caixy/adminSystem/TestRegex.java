package com.caixy.adminSystem;

import com.caixy.adminSystem.utils.EncryptionUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * 测试密码
 *
 * @name: com.caixy.adminSystem.TestRegex
 * @author: CAIXYPROMISE
 * @since: 2024-04-26 20:29
 **/
public class TestRegex
{
    @Test
    public void test()
    {
        Date date = new Date();



        System.out.println(isBoolByInteger(1));
        System.out.println(isBoolByInteger(0));
        System.out.println(isBoolByInteger(2));

    }


    private Boolean isBoolByInteger(Integer value)
    {
        return value != null && value >= 0 && value <= 1;
    }
}
