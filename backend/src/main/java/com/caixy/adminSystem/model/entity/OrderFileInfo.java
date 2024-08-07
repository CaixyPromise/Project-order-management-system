package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.caixy.adminSystem.constant.CommonConstant;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
     * 文件真实名称
     */
    private String fileRealName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String fileSuffix;

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
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = CommonConstant.DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 逻辑删除键（1是，0否）
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public String buildFileName()
    {
        return String.format("%s.%s", this.getFileRealName(), this.getFileSuffix());
    }
}