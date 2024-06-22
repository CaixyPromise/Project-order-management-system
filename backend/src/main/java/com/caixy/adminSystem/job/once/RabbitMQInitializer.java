package com.caixy.adminSystem.job.once;

import com.caixy.adminSystem.config.properties.RabbitMQProperties;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ队列初始化
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.job.once.RabbitMQInitializer
 * @since 2024-06-19 22:49
 **/
@Component
@AllArgsConstructor
public class RabbitMQInitializer implements SmartLifecycle
{
    private final RabbitAdmin rabbitAdmin;
    private final RabbitMQProperties rabbitMQProperties;
    private static boolean isRunning = false;

    @Override
    public void start()
    {
        initializeRabbitMQ();
        isRunning = true;
    }

    @Override
    public void stop()
    {
        // 这里可以添加停止逻辑，如果有的话
        isRunning = false;
    }

    @Override
    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public int getPhase()
    {
        return Integer.MIN_VALUE; // 最早的启动阶段
    }

    private void initializeRabbitMQ()
    {
        // Step 1: Declare all exchanges
        for (RabbitMQProperties.ExchangeConfig exchangeConfig : rabbitMQProperties.getExchanges())
        {
            Exchange exchange = createExchange(exchangeConfig);
            rabbitAdmin.declareExchange(exchange);
        }

        // Step 2: Declare all queues and bindings
        for (RabbitMQProperties.ExchangeConfig exchangeConfig : rabbitMQProperties.getExchanges())
        {
            for (RabbitMQProperties.BindingConfig bindingConfig : exchangeConfig.getBindings())
            {
                // Declare main queue
                Queue queue = createQueue(bindingConfig, exchangeConfig);
                rabbitAdmin.declareQueue(queue);

                // Declare binding of the queue to the exchange
                Binding binding = new Binding(
                        queue.getName(),
                        Binding.DestinationType.QUEUE,
                        exchangeConfig.getName(),
                        bindingConfig.getRoutingKey(),
                        null
                );
                rabbitAdmin.declareBinding(binding);

                // Declare dead-letter queue if configured
                if (bindingConfig.getDeadLetterQueue() != null)
                {
                    Queue dlq = createDeadLetterQueue(bindingConfig.getDeadLetterQueue(), bindingConfig.getDurable());
                    rabbitAdmin.declareQueue(dlq);
                }
            }
        }
    }

    private Exchange createExchange(RabbitMQProperties.ExchangeConfig exchangeConfig)
    {
        switch (exchangeConfig.getType().toLowerCase())
        {
        case RabbitMQProperties.TOPIC_EXCHANGE:
            return ExchangeBuilder.topicExchange(exchangeConfig.getName()).durable(exchangeConfig.getDurable()).build();
        case RabbitMQProperties.DIRECT_EXCHANGE:
            return ExchangeBuilder.directExchange(exchangeConfig.getName()).durable(exchangeConfig.getDurable()).build();
        case RabbitMQProperties.FANOUT_EXCHANGE:
            return ExchangeBuilder.fanoutExchange(exchangeConfig.getName()).durable(exchangeConfig.getDurable()).build();
        case RabbitMQProperties.DELAY_EXCHANGE_TYPE:
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", exchangeConfig.getDelayedType());
            return new CustomExchange(exchangeConfig.getName(), "x-delayed-message", exchangeConfig.getDurable(),
                    exchangeConfig.getAutoDelete(), args);
        default:
            throw new IllegalArgumentException("Unsupported exchange type: " + exchangeConfig.getType());
        }
    }

    private Queue createQueue(RabbitMQProperties.BindingConfig bindingConfig, RabbitMQProperties.ExchangeConfig exchangeConfig)
    {
        Map<String, Object> args = new HashMap<>();
        if (bindingConfig.getDeadLetterQueue() != null)
        {
            // 正确设置死信队列的交换机和路由键
            args.put("x-dead-letter-exchange", exchangeConfig.getName());
            args.put("x-dead-letter-routing-key", bindingConfig.getRoutingKey());
        }

        // 设置延迟时间参数
        if (bindingConfig.getDelayTime() != null && exchangeConfig.getType().equals(RabbitMQProperties.DELAY_EXCHANGE_TYPE))
        {
            args.put(RabbitMQProperties.DELAY_QUEUE_ARGS, bindingConfig.getDelayTime());
        }

        return new Queue(bindingConfig.getQueue(), bindingConfig.getDurable(), bindingConfig.getExclusive(),
                bindingConfig.getAutoDelete(), args);
    }

    private Queue createDeadLetterQueue(String deadLetterQueue, Boolean isDurable)
    {
        return new Queue(deadLetterQueue, isDurable);
    }
}