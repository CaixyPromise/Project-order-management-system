package com.caixy.adminSystem.config.properties.RabbitMQ;

import com.caixy.adminSystem.config.properties.RabbitMQ.core.ExchangeTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RabbitMQ配置类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.config.properties.RabbitMQ.RabbitMQProperties
 * @since 2024-06-19 22:42
 **/
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Data
public class RabbitMQProperties
{
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
         * 对应交换机的死信队列的交换机
         */
        private String deadLetterExchangeName;

        /**
         * 交换机类型
         */
        private ExchangeTypeEnum type;

        /**
         * 对应交换机类型
         */
        private ExchangeTypeEnum deadLetterExchangeType = ExchangeTypeEnum.DIRECT;

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
         * 死信队列路由键
         */
        private String deadLetterRoutingKey;

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
