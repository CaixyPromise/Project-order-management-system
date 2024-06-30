package com.caixy.adminSystem.mq.consumer.exchange.OrderAttachment;

import com.caixy.adminSystem.constant.RabbitConstant;
import com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO;
import com.caixy.adminSystem.model.enums.BackendMessageLevelEnum;
import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.mq.consumer.core.GenericRabbitMQConsumer;
import com.caixy.adminSystem.service.MessageInfoService;
import com.caixy.adminSystem.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单消费者消息类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.exchange.OrderAttachment.OrderAttachmentConsumer
 * @since 2024-06-20 13:17
 **/
@Component
@Slf4j
public class OrderAttachmentConsumer extends GenericRabbitMQConsumer<OrderFileUploadMqDTO>
{
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private MessageInfoService messageInfoService;

    @RabbitListener(queues = RabbitConstant.ORDER_ATTACHMENT_QUEUE, ackMode = "MANUAL")
    @Override
    public void handleMessage(OrderFileUploadMqDTO message, Channel channel, Message rawMessage, String messageId) throws Exception
    {
        // 业务逻辑处理
        Long orderId = message.getOrderId();
        Integer count = message.getFileCount();
        Long currentFileNums = orderInfoService.countUploadOrderAttachment(orderId);
        boolean hasUpload = count.longValue() == currentFileNums;
        log.info("接收到订单附件上传消息：{}", orderId);

        if (!hasUpload)
        {
            // 抛出异常表示处理失败
            log.info("消息id：{}, 订单-[{}]附件上传失败，当前上传{}个，期望上传{}个", messageId, orderId, currentFileNums, count);

            Integer retryCount = getRetryCount(rawMessage);
            // 只有第一次重试才会设置为false
            if (retryCount.equals(0))
            {
                orderInfoService.setOrderValid(orderId, false);
                messageInfoService.sendSystemMessage(
                        "订单附件上传超时",
                        orderId,
                        message.getUserId(),
                        generateMessageTemplate(orderId, currentFileNums, count),
                        BackendMessageLevelEnum.ERROR);
                // 拒绝消息，进入队列重新消费，重试5次
                rejectAndRetryOrDiscard(channel, rawMessage, message, RabbitMQQueueEnum.ORDER_ATTACHMENT, 5);
            }
            else
            {
                // 重试5次后，进入死信队列
                discardMessage(channel, rawMessage.getMessageProperties().getDeliveryTag());
            }
        }
        else
        {
            // 确认消息
            confirmMessage(channel, rawMessage);
            log.info("订单-[{}]附件上传成功", orderId);

        }
    }

    @Override
    @RabbitListener(queues = RabbitConstant.ORDER_ATTACHMENT_DEAD_LETTER_NAME, ackMode = "MANUAL")
    public void handleDeadLetterMessage(OrderFileUploadMqDTO message, Channel channel, Message rawMessage) throws Exception
    {
        // 处理死信队列消息的逻辑
        System.out.println("处理死信队列消息：" + message);
        discardMessage(channel, rawMessage.getMessageProperties().getDeliveryTag());
    }

    private String generateMessageTemplate(Long orderId, Long current, Integer expect)
    {
        return String.format("订单-[%d]附件上传失败，当前上传%d个，期望上传%d个", orderId, current, expect);
    }
}
