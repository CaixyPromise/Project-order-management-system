package com.caixy.adminSystem.model.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新订单信息用数字表示状态的请求
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.dto.order.OrderInfoUpdateCodeRequest
 * @since 2024-06-16 21:16
 **/
@Data
public class OrderInfoUpdateCodeRequest implements Serializable
{
    private Long id;
    /**
     * 是否是对外分配
     */
    private Integer isAssigned;

    /**
     * 是否支付
     */
    private Integer isPaid;


    /**
     * 订单佣金比例
     */
    private Integer orderCommissionRate;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 订单来源
     */
    private Integer orderSource;

    /**
     * 订单联系方式类型
     */
    private Integer customerContactType;

    /**
     * 如果需要的字段有需要修改的文字，可以同步使用content更新文字
     */
    private String content;

    private static final long  serialVersionUID = 1L;
}
