package com.ruantang.service.task.config;

import com.ruantang.commons.constants.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskRabbitConfig {

    /**
     * 声明任务创建队列
     */
    @Bean
    public Queue taskCreateQueue() {
        return QueueBuilder.durable(RabbitConstants.TASK_CREATE_QUEUE)
                .deadLetterExchange(RabbitConstants.TASK_DLQ_EXCHANGE) // 使用任务服务自己的死信队列
                .deadLetterRoutingKey(RabbitConstants.TASK_DLQ_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定到工作流服务的交换机
     */
    @Bean
    public Binding taskCreateBinding() {
        return BindingBuilder.bind(taskCreateQueue())
                .to(new DirectExchange(RabbitConstants.TASK_CREATE_EXCHANGE))
                .with(RabbitConstants.TASK_CREATE_ROUTING_KEY);
    }

    // 任务服务的死信队列配置（可选）
    @Bean
    public DirectExchange taskDlqExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.TASK_DLQ_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue taskDlqQueue() {
        return QueueBuilder.durable(RabbitConstants.TASK_DLQ_QUEUE).build();
    }

    @Bean
    public Binding taskDlqBinding() {
        return BindingBuilder.bind(taskDlqQueue())
                .to(taskDlqExchange())
                .with(RabbitConstants.TASK_DLQ_ROUTING_KEY);
    }
}
