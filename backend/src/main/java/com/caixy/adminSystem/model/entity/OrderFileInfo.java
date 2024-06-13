package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单附件文件信息表
 *
 * @TableName order_file_info
 */
@TableName(value = "order_file_info")
@Data
public class OrderFileInfo implements Serializable
{
    /**
     * 文件ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小（单位：字节）
     */
    private Long fileSize;

    /**
     * 文件sha256值
     */
    private String fileSha256;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除键（1是，0否）
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}