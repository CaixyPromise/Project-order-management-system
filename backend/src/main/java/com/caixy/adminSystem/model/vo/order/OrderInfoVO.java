package com.caixy.adminSystem.model.vo.order;

import com.caixy.adminSystem.model.vo.file.OrderFileVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单信息VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.order.OrderInfoPageVO
 * @since 2024-06-04 20:56
 **/
@Data
public class OrderInfoVO implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单名称描述
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
    private String categoryName;

    /**
     * 订单标签
     */
    private String orderTags;

    /**
     * 订单编程语言ID
     */
    private String langName;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     * 是否包含订单附件
     */
    private Integer orderAttachmentNum;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isValid;

    /**
     * 订单附件列表
     */
    private List<OrderFileVO> orderAttachmentList;

    private static final long serialVersionUID = 1L;
}
