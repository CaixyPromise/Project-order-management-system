package com.caixy.adminSystem.constant;

/**
 * @name: com.caixy.adminSystem.constant.RabbitConstant
 * @description: 消息队列Topic常量
 * @author: CAIXYPROMISE
 * @date: 2024-06-14 17:05
 **/
public interface RabbitConstant
{
    String ORDER_ATTACHMENT_NAME = "orderDelayAttachmentQueue";
    String ORDER_ATTACHMENT_DEAD_LETTER_NAME = "X-DeadLetter-Attachment-Queue";
}
