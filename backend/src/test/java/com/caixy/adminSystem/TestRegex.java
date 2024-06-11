package com.caixy.adminSystem;

import com.caixy.adminSystem.utils.EncryptionUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        List<String> strList = Arrays.asList("1", "2", "3");
        String join = String.join(",", strList);
        System.out.println(join);
    }



}
