package com.caixy.adminSystem.mq.producer.exchange.OrderInfo;

import com.caixy.adminSystem.model.enums.RabbitMQQueueEnum;
import com.caixy.adminSystem.mq.producer.core.GenericRabbitMQProducer;
import com.caixy.adminSystem.mq.producer.core.RabbitProducer;
import org.springframework.stereotype.Component;

/**
 * 订单信息异步更新ES消息生产者
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.producer.exchange.OrderInfo.OrderInfoSaveProducer
 * @since 2024-06-30 15:46
 **/
@Component
@RabbitProducer(RabbitMQQueueEnum.ORDER_ASYNC_SAVE_ES)
public class OrderInfoSaveProducer extends GenericRabbitMQProducer<Long>
{
}
