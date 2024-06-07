package com.caixy.adminSystem.model.dto.category;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建订单分类请求
 *


 */
@Data
public class OrderCategoryAddRequest implements Serializable
{
    /**
     * 订单分类名称
     */
    private String categoryName;

    /**
     * 订单分类描述
     */
    private String categoryDesc;

    private static final long serialVersionUID = 1L;
}