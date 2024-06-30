package com.caixy.adminSystem.constant;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;

/**
 * @name: com.caixy.adminSystem.constant.RabbitConstant
 * @description: 消息队列Topic常量
 * @author: CAIXYPROMISE
 * @date: 2024-06-14 17:05
 **/
public interface RabbitConstant
{
    String ORDER_ATTACHMENT_QUEUE = "orderDelayAttachmentQueue";
    String ORDER_ATTACHMENT_DEAD_LETTER_NAME = "X-DeadLetter-Attachment-Queue";

    /**
     * 订单保存队列名称
     */
    String ORDER_SAVE_QUEUE = "orderSaveQueue";
    String ORDER_SAVE_DEAD_LETTER_QUEUE = "X-DeadLetter-Save-Queue";
}
