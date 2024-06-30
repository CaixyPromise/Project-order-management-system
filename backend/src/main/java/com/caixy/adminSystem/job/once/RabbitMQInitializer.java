package com.caixy.adminSystem.job.once;

import com.caixy.adminSystem.config.properties.RabbitMQ.RabbitMQProperties;
import com.caixy.adminSystem.config.properties.RabbitMQ.core.ExchangeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQInitializer implements SmartLifecycle
{
    private final RabbitAdmin rabbitAdmin;
    private final RabbitMQProperties rabbitMQProperties;
    private static boolean isRunning = false;

    @Override
    public void start()
    {
        try
        {
            initializeRabbitMQ();
            isRunning = true;
        }
        catch (Exception e)
        {
            log.error("Failed to initialize RabbitMQ: {}", e.getMessage(), e);
        }
    }

    @Override
    public void stop()
    {
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
        return Integer.MIN_VALUE;
    }

    private void initializeRabbitMQ()
    {
        declareDeadLetterExchanges();
        declareExchangesAndQueues();
    }

    private void declareDeadLetterExchanges()
    {
        rabbitMQProperties.getExchanges().stream()
                .filter(exchange -> StringUtils.isNotBlank(exchange.getDeadLetterExchangeName()))
                .forEach(exchange -> {
                    Exchange dlExchange = createExchange(
                            exchange, exchange.getDeadLetterExchangeName(), exchange.getDeadLetterExchangeType()
                    );
                    rabbitAdmin.declareExchange(dlExchange);
                    log.info("Dead letter exchange [{}] created successfully", exchange.getDeadLetterExchangeName());
                });
    }

    private void declareExchangesAndQueues()
    {
        rabbitMQProperties.getExchanges().forEach(exchangeConfig -> {
            // Create and declare each exchange
            Exchange exchange = createExchange(exchangeConfig, exchangeConfig.getName(), exchangeConfig.getType());
            rabbitAdmin.declareExchange(exchange);
            log.info("Exchange [{}] created successfully", exchangeConfig.getName());

            // Create and declare queues and bindings
            exchangeConfig.getBindings().forEach(bindingConfig -> {
                Queue queue = createQueue(bindingConfig, exchangeConfig);
                rabbitAdmin.declareQueue(queue);
                log.info("Queue [{}] created successfully", queue.getName());

                Binding binding = new Binding(
                        queue.getName(), Binding.DestinationType.QUEUE, exchangeConfig.getName(),
                        bindingConfig.getRoutingKey(), null
                );
                rabbitAdmin.declareBinding(binding);
                log.info("Binding between exchange [{}] and queue [{}] created successfully", exchangeConfig.getName(), queue.getName());

                // Handle dead letter queue if specified
                if (StringUtils.isNotBlank(bindingConfig.getDeadLetterQueue()))
                {
                    declareDeadLetterQueue(bindingConfig, exchangeConfig);
                }
            });
        });
    }

    private void declareDeadLetterQueue(RabbitMQProperties.BindingConfig bindingConfig, RabbitMQProperties.ExchangeConfig exchangeConfig)
    {
        Queue dlq = new Queue(bindingConfig.getDeadLetterQueue(), bindingConfig.getDurable());
        rabbitAdmin.declareQueue(dlq);
        log.info("Dead letter queue [{}] created successfully", dlq.getName());

        Binding dlqBinding = new Binding(
                dlq.getName(), Binding.DestinationType.QUEUE, exchangeConfig.getDeadLetterExchangeName(),
                bindingConfig.getDeadLetterRoutingKey(), null
        );
        rabbitAdmin.declareBinding(dlqBinding);
        log.info("Binding between dead letter exchange [{}] and queue [{}] created successfully", exchangeConfig.getDeadLetterExchangeName(), dlq.getName());
    }

    private Exchange createExchange(RabbitMQProperties.ExchangeConfig exchangeConfig, String exchangeName, ExchangeTypeEnum exchangeType)
    {
        // Depending on the type, create a specific type of exchange
        switch (exchangeType)
        {
        case DIRECT:
            return ExchangeBuilder.directExchange(exchangeName).durable(exchangeConfig.getDurable()).build();
        case TOPIC:
            return ExchangeBuilder.topicExchange(exchangeName).durable(exchangeConfig.getDurable()).build();
        case FANOUT:
            return ExchangeBuilder.fanoutExchange(exchangeName).durable(exchangeConfig.getDurable()).build();
        case HEADERS:
            return ExchangeBuilder.headersExchange(exchangeName).durable(exchangeConfig.getDurable()).build();
        case DELAY:
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", exchangeConfig.getDelayedType());
            return new CustomExchange(exchangeName, "x-delayed-message", exchangeConfig.getDurable(),
                    exchangeConfig.getAutoDelete(), args);
        default:
            throw new IllegalArgumentException("Unsupported exchange type: " + exchangeType);
        }
    }

    private Queue createQueue(RabbitMQProperties.BindingConfig bindingConfig, RabbitMQProperties.ExchangeConfig exchangeConfig)
    {
        Map<String, Object> args = new HashMap<>();
        if (StringUtils.isNotBlank(bindingConfig.getDeadLetterQueue()))
        {
            args.put("x-dead-letter-exchange", exchangeConfig.getDeadLetterExchangeName());
            args.put("x-dead-letter-routing-key", bindingConfig.getDeadLetterRoutingKey());
        }

        if (bindingConfig.getDelayTime() != null && exchangeConfig.getType() == ExchangeTypeEnum.DELAY)
        {
            args.put("x-delay", bindingConfig.getDelayTime());
        }

        return new Queue(bindingConfig.getQueue(), bindingConfig.getDurable(), bindingConfig.getExclusive(),
                bindingConfig.getAutoDelete(), args);
    }
}
