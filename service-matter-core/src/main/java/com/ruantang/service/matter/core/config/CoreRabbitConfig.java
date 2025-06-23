package com.ruantang.service.matter.core.config;

import com.ruantang.commons.constants.RabbitConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CoreRabbitConfig {
    /**
     * 创建状态更新交换机
     * @return
     */
    @Bean
    public DirectExchange statusExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.STATUS_EXCHANGE).durable(true).build();
    }



    /**
     * 创建死信交换机
     * @return
     */
    @Bean
    public DirectExchange coreDlqExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.CORE_DLQ_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue coreDlqQueue() {
        return QueueBuilder.durable(RabbitConstants.CORE_DLQ_QUEUE).build();
    }

    @Bean
    public Binding coreDlqBinding(
            @Qualifier("coreDlqExchange") DirectExchange dlqExchange, // 明确指定Bean名称
            @Qualifier("coreDlqQueue") Queue dlqQueue) {
        return BindingBuilder.bind(dlqQueue).to(dlqExchange).with(RabbitConstants.CORE_DLQ_ROUTING_KEY);
    }
}
