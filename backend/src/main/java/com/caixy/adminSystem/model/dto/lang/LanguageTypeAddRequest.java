package com.caixy.adminSystem.model.dto.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建语言类型请求
 *
 */
@Data
public class LanguageTypeAddRequest implements Serializable
{
    /**
     * 编程语言名称
     */
    private String languageName;

    private static final long serialVersionUID = 1L;
}