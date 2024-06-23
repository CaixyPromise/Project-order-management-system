package com.caixy.adminSystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.RabbitMQConfig
 * @since 2024-06-18 21:33
 **/
@Configuration
@Slf4j
public class RabbitMQConfig
{
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    //配置连接工厂
    @Bean
    public CachingConnectionFactory cachingConnectionFactory()
    {
        return new CachingConnectionFactory(rabbitmqHost);
    }

    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        //需要传入
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory());
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(ConnectionFactory connectionFactory)
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置为手动确认
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory)
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData != null)
            {
                String id = correlationData.getId();
                if (ack)
                {
                    System.out.println("Message sent successfully: " + id);
                }
                else
                {
                    System.out.println("Message failed to send: " + id + ", cause: " + cause);
                }
            }
        });

        // 设置返回回调，用于处理消息没有路由到队列的情况
        rabbitTemplate.setReturnsCallback(returned -> {
            log.info("Message returned: {}", returned.getMessage());
            log.info("Exchange: {}", returned.getExchange());
            log.info("RoutingKey: {}", returned.getRoutingKey());
            log.info("ReplyText: {}", returned.getReplyText());
        });

        return rabbitTemplate;
    }

}
