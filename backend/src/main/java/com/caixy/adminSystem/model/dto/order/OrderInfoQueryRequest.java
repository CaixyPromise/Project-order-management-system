package com.caixy.adminSystem.model.dto.order;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoQueryRequest extends PageRequest implements Serializable
{
    /**
     * 系统id
     */
    private Long id;


    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单总额
     */
    private BigDecimal amount;

    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 语言名称
     */

    private Long langId;

    /**
     * 分类名称
     */
    private Long orderCategoryId;
    /**
     * 订单状态
     */
    private Integer orderStatus;
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

    /**
     * 订单联系方式
     */
    private String customerContact;

    /**
     * 顾客邮箱
     */
    private String customerEmail;

    /**
     * 订单分配人微信Id
     */
    private String orderAssignToWxId;

    private static final long serialVersionUID = 1L;
}