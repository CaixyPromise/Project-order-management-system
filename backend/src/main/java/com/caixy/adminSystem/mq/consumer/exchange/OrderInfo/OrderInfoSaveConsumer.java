package com.caixy.adminSystem.mq.consumer.exchange.OrderInfo;

import com.caixy.adminSystem.constant.RabbitConstant;
import com.caixy.adminSystem.esdao.OrderEsRepository;
import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.entity.OrderInfo;
import com.caixy.adminSystem.mq.consumer.core.GenericRabbitMQConsumer;
import com.caixy.adminSystem.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 订单信息更新/添加消费队列
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.mq.exchange.OrderInfo.OrderInfoSaveConsumer
 * @since 2024-06-29 15:45
 **/
@Component
@Slf4j
public class OrderInfoSaveConsumer extends GenericRabbitMQConsumer<Long>
{
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private OrderEsRepository orderEsRepository;

    @RabbitListener(queues = RabbitConstant.ORDER_SAVE_QUEUE, ackMode = "MANUAL")
    @Override
    public void handleMessage(Long message, Channel channel, Message rawMessage, String messageId) throws Exception
    {
        List<OrderInfo> orderInfoList = orderInfoService.listByIds(Collections.singletonList(message));
        List<OrderInfoEsDTO> orderInfoEsDTOList = orderInfoService.getOrderInfoEsDTOList(orderInfoList);
        if (orderInfoEsDTOList.isEmpty())
        {
            log.warn("订单信息更新/添加消费队列, 订单信息为空, 消息ID: {}", rawMessage.getMessageProperties().getMessageId());
            rejectMessage(channel, rawMessage, false);
        }
        orderEsRepository.saveAll(orderInfoEsDTOList);
        confirmMessage(channel, rawMessage);
        log.info("订单信息更新/添加消费队列, 成功, 消息ID: {}", rawMessage.getMessageProperties().getMessageId());
    }

    @RabbitListener(queues = RabbitConstant.ORDER_SAVE_DEAD_LETTER_QUEUE)
    @Override
    public void handleDeadLetterMessage(Long message, Channel channel, Message rawMessage) throws Exception
    {
        log.warn("订单信息更新/添加消费队列, 死信队列, 消息ID: {}", rawMessage.getMessageProperties().getMessageId());
        confirmMessage(channel, rawMessage);
    }
}
