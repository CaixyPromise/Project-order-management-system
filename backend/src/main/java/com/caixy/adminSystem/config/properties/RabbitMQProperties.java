package com.caixy.adminSystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RabbitMQ配置类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.properties.RabbitMQProperties
 * @since 2024-06-19 22:42
 **/
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Data
public class RabbitMQProperties
{
    public static final String DIRECT_EXCHANGE = "direct";

    public static final String TOPIC_EXCHANGE = "topic";

    public static final String DELAY_EXCHANGE_TYPE = "x-delayed-message";

    public static final String FANOUT_EXCHANGE = "fanout";

    public static final String DELAY_QUEUE_ARGS = "x-delay";

    /**
     * 交换机配置队列
     */
    private List<ExchangeConfig> exchanges;

    /**
     * 交换机配置
     */
    @Data
    public static class ExchangeConfig
    {
        /**
         * 交换机名称
         */
        private String name;
        /**
         * 交换机类型
         */
        private String type;
        /**
         * 绑定信息
         */
        private List<BindingConfig> bindings;

        /**
         * 延迟队列交换机类型
         */
        private String delayedType;

        private Boolean durable = true;

        private Boolean autoDelete = false;
    }

    /**
     * 队列+路由键+死信队列
     */
    @Data
    public static class BindingConfig
    {
        /**
         * 队列名称
         */
        private String queue;
        /**
         * 路由键
         */
        private String routingKey;
        /**
         * 死信队列名称
         */
        private String deadLetterQueue;

        /**
         * 如果是延迟队列，需要设置过期时间
         */
        private Long delayTime = 10000L;

        private Boolean durable = true;

        private Boolean autoDelete = false;

        private Boolean exclusive = false;
    }
}
