package com.caixy.adminSystem.model.vo.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单文件信息VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.file.OrderFileVO
 * @since 2024-06-16 10:06
 **/
@Data
public class OrderFileVO implements Serializable
{
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

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
    private String creatorName;

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
    private static final long serialVersionUID = -1L;
}
