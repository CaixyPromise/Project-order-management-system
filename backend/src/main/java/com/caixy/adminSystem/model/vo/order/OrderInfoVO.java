package com.caixy.adminSystem.model.vo.order;

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
     * 订单平台id
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
     * 是否是对外分配
     */
    private Boolean isAssigned;

    /**
     * 是否支付
     */
    private Boolean isPaid;

    /**
     * 是否包含附件
     */
    private Boolean hasOrderAttachment;


    /**
     * 订单来源
     */
    private String orderSource;


    /**
     * 订单分类名称
     */
    private String orderCategoryName;


    /**
     * 订单编程语言ID
     */
    private String langName;


    /**
     * 交付截止日期
     */
    private Date orderDeadline;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单附件列表
     */
    private List<OrderAttachmentList> orderAttachmentList;

    @Data
    public static class OrderAttachmentList implements Serializable
    {
        /**
         * 文件名
         */
        private String fileName;

        /**
         * 文件大小（单位：字节）
         */
        private Long fileSize;

        /**
         * 创建人名称
         */
        private String creatorName;

        /**
         * 下载链接
         */
        private String downloadUrl;

        private static final long serialVersionUID = 1L;
    }
    private static final long serialVersionUID = 1L;
}
