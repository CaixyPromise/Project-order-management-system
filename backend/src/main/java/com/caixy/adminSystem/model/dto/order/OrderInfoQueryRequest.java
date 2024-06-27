package com.caixy.adminSystem.model.dto.order;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoQueryRequest extends PageRequest implements Serializable
{
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 创建人id
     */
    private String orderTitle;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 语言名称
     */
    private String langName;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 是否分配
     */
    private Boolean isAssigned;
    /**
     * 是否支付
     */
    private Boolean isPaid;
    /**
     * 订单来源
     */
    private String orderSource;

    private static final long serialVersionUID = 1L;
}