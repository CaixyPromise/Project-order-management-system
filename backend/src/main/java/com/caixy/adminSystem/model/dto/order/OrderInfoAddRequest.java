package com.caixy.adminSystem.model.dto.order;

import com.caixy.adminSystem.model.dto.file.UploadFileInfoDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class OrderInfoAddRequest implements Serializable
{
    /**
     * 订单平台id
     */
    private String orderId;

    /**
     * 订单描述标题
     */
    private String orderTitle;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 已支付金额
     */
    private BigDecimal amountPaid;

    /**
     * 订单联系方式类型
     */
    private Integer customerContactType;

    /**
     * 订单联系方式
     */
    private String customerContact;

    /**
     * 顾客邮箱
     */
    private String customerEmail;

    /**
     * 是否是对外分配
     */
    private Integer isAssigned;

    /**
     * 是否支付
     */
    private Integer isPaid;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 订单来源
     */
    private Integer orderSource;

    /**
     * 订单分配人微信Id
     */
    private String orderAssignToWxId;

    /**
     * 订单佣金比例
     */
    private Integer orderCommissionRate;

    /**
     * 订单分类
     */
    private Long orderCategoryId;

    /**
     * 订单标签
     */
    private List<String> orderTag;

    /**
     * 订单编程语言
     */
    private Long orderLangId;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     * 订单附件
     */
    private List<UploadFileInfoDTO> attachmentList;

    /**
     * 订单备注
     */
    private String orderRemark;

    /**
     * 交付截止日期
     */
    private Date orderDeadline;

    /**
     * 订单完成时间
     */
    private Date orderCompletionTime;

    /**
     * 订单开始日期
     */
    private Date orderStartDate;

    /**
     * 订单结束日期
     */
    private Date orderEndDate;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    private static final long serialVersionUID = 1L;
}