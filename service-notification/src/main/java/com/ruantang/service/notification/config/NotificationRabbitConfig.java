package com.ruantang.service.notification.config;

import com.ruantang.commons.constants.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitConfig {

    /**
     * 声明通知发送队列
     */
    @Bean
    public Queue notificationSendQueue() {
        return QueueBuilder.durable(RabbitConstants.NOTIFICATION_SEND_QUEUE)
                .deadLetterExchange(RabbitConstants.NOTIFICATION_DLQ_EXCHANGE)
                .deadLetterRoutingKey(RabbitConstants.NOTIFICATION_DLQ_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定到工作流服务的交换机
     */
    @Bean
    public Binding notificationSendBinding() {
        return BindingBuilder.bind(notificationSendQueue())
                .to(new DirectExchange(RabbitConstants.NOTIFICATION_SEND_EXCHANGE))
                .with(RabbitConstants.NOTIFICATION_SEND_ROUTING_KEY);
    }

    // 通知服务的死信队列配置
    @Bean
    public DirectExchange notificationDlqExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.NOTIFICATION_DLQ_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue notificationDlqQueue() {
        return QueueBuilder.durable(RabbitConstants.NOTIFICATION_DLQ_QUEUE).build();
    }

    @Bean
    public Binding notificationDlqBinding() {
        return BindingBuilder.bind(notificationDlqQueue())
                .to(notificationDlqExchange())
                .with(RabbitConstants.NOTIFICATION_DLQ_ROUTING_KEY);
    }
}