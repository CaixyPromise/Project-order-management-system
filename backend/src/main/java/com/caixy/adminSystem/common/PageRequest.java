package com.caixy.adminSystem.common;

import com.caixy.adminSystem.constant.CommonConstant;
import lombok.Data;

import java.util.List;

/**
 * 分页请求
 *
 
 */
@Data
public class PageRequest
{

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

    /**
     * searchAfter 参数，用于深度分页
     */
    private List<Object> searchAfter;
}
