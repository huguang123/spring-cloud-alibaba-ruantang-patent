package com.ruantang.service.deadline.config;

import com.ruantang.commons.constants.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadlineRabbitConfig {

    /**
     * 声明期限设置队列
     */
    @Bean
    public Queue deadlineSetQueue() {
        return QueueBuilder.durable(RabbitConstants.DEADLINE_SET_QUEUE)
                .deadLetterExchange(RabbitConstants.DEADLINE_DLQ_EXCHANGE)
                .deadLetterRoutingKey(RabbitConstants.DEADLINE_DLQ_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定到工作流服务的交换机
     */
    @Bean
    public Binding deadlineSetBinding() {
        return BindingBuilder.bind(deadlineSetQueue())
                .to(new DirectExchange(RabbitConstants.DEADLINE_SET_EXCHANGE))
                .with(RabbitConstants.DEADLINE_SET_ROUTING_KEY);
    }

    // 期限服务的死信队列配置
    @Bean
    public DirectExchange deadlineDlqExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DEADLINE_DLQ_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue deadlineDlqQueue() {
        return QueueBuilder.durable(RabbitConstants.DEADLINE_DLQ_QUEUE).build();
    }

    @Bean
    public Binding deadlineDlqBinding() {
        return BindingBuilder.bind(deadlineDlqQueue())
                .to(deadlineDlqExchange())
                .with(RabbitConstants.DEADLINE_DLQ_ROUTING_KEY);
    }
}