package com.caixy.adminSystem.model.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 站内消息信息表
 * @TableName message_info
 */
@TableName(value ="message_info")
@Data
public class MessageInfo implements Serializable {
    /**
     * 消息id
     */
    @TableId
    private Long id;

    /**
     * 消息主题
     */
    private String subject;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 发信人id
     */
    private Long fromUser;

    /**
     * 接受消息人id
     */
    private Long forUser;

    /**
     * 消息等级
     */
    private Integer level;

    /**
     * 是否已处理
     */
    private Integer isHandled;

    /**
     * 添加时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}