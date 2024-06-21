package com.caixy.adminSystem.mq.consumer;

import com.caixy.adminSystem.constant.RabbitConstant;
import com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO;
import com.caixy.adminSystem.model.enums.BackendMessageLevelEnum;
import com.caixy.adminSystem.mq.consumer.core.GenericRabbitMQConsumer;
import com.caixy.adminSystem.service.MessageInfoService;
import com.caixy.adminSystem.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单消费者消息类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.OrderAttachmentConsumer
 * @since 2024-06-20 13:17
 **/
@Service
@Slf4j
public class OrderAttachmentConsumer extends GenericRabbitMQConsumer<OrderFileUploadMqDTO>
{
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private MessageInfoService messageInfoService;


    @RabbitListener(queues = RabbitConstant.ORDER_ATTACHMENT_NAME, ackMode = "MANUAL")
    @Override
    public void handleMessage(OrderFileUploadMqDTO message, Channel channel, Message rawMessage) throws Exception
    {
        // 业务逻辑处理
        Long orderId = message.getOrderId();
        Integer count = message.getFileCount();
        Long currentFileNums = orderInfoService.countUploadOrderAttachment(orderId);
        boolean hasUpload = count.longValue() == currentFileNums;
        log.info("接收到订单附件上传消息：{}", orderId);

        if (!hasUpload)
        {
            orderInfoService.setOrderValid(orderId, false);
            messageInfoService.sendSystemMessage(
                    orderId,
                    message.getUserId(),
                    generateMessageTemplate(orderId, currentFileNums, count),
                    BackendMessageLevelEnum.ERROR
            );
            // 抛出异常表示处理失败
            log.info("订单-[{}]附件上传失败，当前上传{}个，期望上传{}个", orderId, currentFileNums, count);
        }
        else
        {
            // 确认消息
            confirmMessage(channel, rawMessage.getMessageProperties().getDeliveryTag());
            log.info("订单-[{}]附件上传成功", orderId);
        }
    }

    @Override
    public void handleDeadLetterMessage(OrderFileUploadMqDTO message, Channel channel, Message rawMessage) throws Exception
    {
        // 处理死信队列消息的逻辑
        System.out.println("处理死信队列消息：" + message);
    }

    private String generateMessageTemplate(Long orderId, Long current, Integer expect)
    {
        return String.format("订单-[%d]附件上传失败，当前上传%d个，期望上传%d个", orderId, current, expect);
    }
}
