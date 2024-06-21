package com.caixy.adminSystem.mq.producer;

import com.caixy.adminSystem.model.dto.order.OrderFileUploadMqDTO;
import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.mq.producer.core.GenericRabbitMQProducer;
import com.caixy.adminSystem.utils.RabbitMQUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 订单附件消息生产者
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.producer.OrderAttachmentProducer
 * @since 2024-06-20 14:47
 **/
@Component
public class OrderAttachmentProducer extends GenericRabbitMQProducer<OrderFileUploadMqDTO>
{
    @Override
    public void sendMessage(RabbitMQQueueEnum queueEnum, OrderFileUploadMqDTO message)
    {
        rabbitMQUtils.sendDelayedMessage(RabbitMQQueueEnum.ORDER_ATTACHMENT, message);
    }
}
