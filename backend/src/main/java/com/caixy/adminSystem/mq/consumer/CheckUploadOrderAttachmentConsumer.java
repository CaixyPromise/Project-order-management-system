package com.caixy.adminSystem.mq.consumer;

import com.caixy.adminSystem.constant.RocketConstant;
import com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO;
import com.caixy.adminSystem.model.enums.BackendMessageLevelEnum;
import com.caixy.adminSystem.service.MessageInfoService;
import com.caixy.adminSystem.service.OrderInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 检查订单附件是否正确上传延迟队列
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.consumer.CheckUploadOrderAttachmentConsumer
 * @since 2024-06-14 17:00
 **/
@Service
@RocketMQMessageListener(topic = RocketConstant.ORDER_ATTACHMENT_TOPIC,
        consumerGroup = RocketConstant.ORDER_ATTACHMENT_TOPIC,
        maxReconsumeTimes = 3
)
public class CheckUploadOrderAttachmentConsumer implements RocketMQListener<OrderFileUploadMqDTO>
{
    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private MessageInfoService messageInfoService;

    @Override
    public void onMessage(OrderFileUploadMqDTO orderInfo)
    {
        Long orderId = orderInfo.getOrderId();
        Integer count = orderInfo.getFileCount();
        Long currentFileNums = orderInfoService.countUploadOrderAttachment(orderId);
        boolean hasUpload = count.longValue() == currentFileNums;
        if (!hasUpload)
        {
            orderInfoService.setOrderValid(orderId, false);
            messageInfoService.sendSystemMessage(
                    orderId,
                    orderInfo.getUserId(),
                    generateMessageTemplate(orderId, currentFileNums, count),
                    BackendMessageLevelEnum.ERROR
                    );
            throw new RuntimeException("订单附件上传失败");
        }
    }

    private String generateMessageTemplate(Long orderId, Long current, Integer expect)
    {
        return String.format("订单-[%d]附件上传失败，当前上传%d个，期望上传%d个", orderId, current, expect);
    }
}
