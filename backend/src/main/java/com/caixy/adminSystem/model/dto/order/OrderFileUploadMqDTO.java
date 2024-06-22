package com.caixy.adminSystem.model.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单文件上传信息消息队列实体类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO
 * @since 2024-06-14 20:40
 **/
@Data
public class OrderFileUploadMqDTO implements Serializable
{
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 期待文件数量
     */
    private Integer fileCount;
    /**
     * 用户id
     */
    private Long userId;

    private static final long SerialVersionUID = 1L;
}
