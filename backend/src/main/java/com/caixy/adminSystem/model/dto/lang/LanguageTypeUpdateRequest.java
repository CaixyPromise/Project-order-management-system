package com.caixy.adminSystem.model.dto.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新语言类型请求
 */
@Data
public class LanguageTypeUpdateRequest implements Serializable
{
    /**
     * 修改的Id
     */
    private Long id;

    /**
     * 编程语言名称
     */
    private String languageName;

    private static final long serialVersionUID = 1L;
}