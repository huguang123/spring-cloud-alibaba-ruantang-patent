package com.ruantang.service.matter.workflow.config;

import com.ruantang.commons.constants.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowRabbitConfig {

    // 跨服务导致需要延时绑定队列
    private final RabbitAdmin rabbitAdmin;

    public WorkflowRabbitConfig(ConnectionFactory connectionFactory) {
        this.rabbitAdmin = new RabbitAdmin(connectionFactory);
    }

    // ======================= 消费队列配置 =======================

    /**
     * 声明消费队列（绑定到核心服务的交换机）
     * 配置死信队列为工作流专属死信队列
     */

    @Bean
    public Queue statusQueue() {
        return QueueBuilder.durable(RabbitConstants.STATUS_QUEUE)
                .deadLetterExchange(RabbitConstants.CORE_DLQ_EXCHANGE) // 引用死信交换机
                .deadLetterRoutingKey(RabbitConstants.CORE_DLQ_ROUTING_KEY)
                .build();
    }

    //下游服务延时绑定
    @Bean
    public Binding statusBinding(@Qualifier("statusQueue") Queue statusQueue) {
        return BindingBuilder.bind(statusQueue)
                .to(new DirectExchange(RabbitConstants.STATUS_EXCHANGE))
                .with(RabbitConstants.STATUS_ROUTING_KEY);
    }




    // ======================= 发送给下游服务的交换机 =======================

    /**
     * 任务创建事件交换机
     */
    @Bean
    public DirectExchange taskCreateExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.TASK_CREATE_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 通知发送事件交换机
     */
    @Bean
    public DirectExchange notificationSendExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.NOTIFICATION_SEND_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 期限设置事件交换机
     */
    @Bean
    public DirectExchange deadlineSetExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DEADLINE_SET_EXCHANGE)
                .durable(true)
                .build();
    }


    // ======================= 死信队列配置 =======================

    /**
     * 工作流专属死信交换机
     */
    @Bean
    public DirectExchange workflowDlqExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.WORKFLOW_DLQ_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 工作流专属死信队列
     */
    @Bean
    public Queue workflowDlqQueue() {
        return QueueBuilder.durable(RabbitConstants.WORKFLOW_DLQ_QUEUE).build();
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding workflowDlqBinding() {
        return BindingBuilder.bind(workflowDlqQueue())
                .to(workflowDlqExchange())
                .with(RabbitConstants.WORKFLOW_DLQ_ROUTING_KEY);
    }
}




// 后期增加监控服务添加：
/*
@Component
public class BindingMonitor {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    public void checkBindings() {
        checkBinding(RabbitConstants.TASK_CREATE_QUEUE,
                     RabbitConstants.TASK_CREATE_EXCHANGE,
                     RabbitConstants.TASK_CREATE_ROUTING_KEY);
    }

    private void checkBinding(String queue, String exchange, String routingKey) {
        Binding binding = new Binding(
            queue,
            Binding.DestinationType.QUEUE,
            exchange,
            routingKey,
            null
        );

        if (!rabbitAdmin.getQueueInfo(queue).isPresent()) {
            rabbitAdmin.declareQueue(new Queue(queue, true));
        }

        try {
            rabbitAdmin.declareBinding(binding);
        } catch (AmqpException e) {
            log.error("绑定失败: {}->{}. 将重试...", queue, exchange);
            // 发送告警通知
            alertService.sendCriticalAlert("关键绑定缺失: " + binding);
        }
    }
}
 */
