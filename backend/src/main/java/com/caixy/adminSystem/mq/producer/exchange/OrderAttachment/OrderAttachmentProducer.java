package com.caixy.adminSystem.mq.producer.exchange.OrderAttachment;

import com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO;
import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.mq.producer.core.GenericRabbitMQProducer;
import com.caixy.adminSystem.mq.producer.core.RabbitProducer;
import org.springframework.stereotype.Component;

/**
 * 订单附件消息生产者
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.producer.OrderAttachment.OrderAttachmentProducer
 * @since 2024-06-20 14:47
 **/
@Component
@RabbitProducer(RabbitMQQueueEnum.ORDER_ATTACHMENT)
public class OrderAttachmentProducer extends GenericRabbitMQProducer<OrderFileUploadMqDTO>
{
    /**
     * 发送延迟消息（该队列默认都是发延迟消息）
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/30 下午4:05
     */
    @Override
    public void sendMessage(OrderFileUploadMqDTO message)
    {
        rabbitMQUtils.sendDelayedMessage(RabbitMQQueueEnum.ORDER_ATTACHMENT, message);
    }
}
