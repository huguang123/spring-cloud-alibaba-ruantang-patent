package com.ruantang.service.matter.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.commons.enums.MessageType;
import com.ruantang.commons.exception.message.MessageDeserializationException;
import com.ruantang.commons.exception.message.UnexpectedMessageTypeException;
import com.ruantang.commons.service.IdempotencyService;
import com.ruantang.service.matter.workflow.event.CaseStatusEvent;
import com.ruantang.service.matter.workflow.monitoring.ConsumerMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class CaseStatusEventConsumer {
    private final ObjectMapper objectMapper;
    private final IdempotencyService idempotencyService;
//    private final CaseStatusService caseStatusService;
    private final ConsumerMetrics consumerMetrics;

    /**
     * 消费案件状态变更事件
     *
     * @param message RabbitMQ消息对象
     * @param channel RabbitMQ通道
     * @param tag 消息投递标签
     */
    @RabbitListener(
            queues = RabbitConstants.STATUS_QUEUE,
            ackMode = "MANUAL"
    )
    public void consumeCaseStatusEvent(
            Message message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {
        final Instant startTime = Instant.now();
        final String messageId = extractMessageId(message);
        MDC.put("messageId", messageId); // 日志上下文追踪

        try {
            // 1. 验证消息协议
            validateMessageProtocol(message);

            // 2. 反序列化消息体
            CaseStatusEvent event = deserializeMessage(message);

            // 3. 幂等性检查
            if (idempotencyService.isProcessed(messageId)) {
                log.debug("消息已处理，跳过");
                acknowledgeMessage(channel, tag);
                consumerMetrics.recordIdempotentSkip();
                return;
            }

            // 4. 处理业务逻辑
            processBusinessLogic(event);

            // 5. 标记为已处理
            idempotencyService.markAsProcessed(messageId);
            acknowledgeMessage(channel, tag);

            // 记录成功指标
            final Duration duration = Duration.between(startTime, Instant.now());
            consumerMetrics.recordSuccess(duration.toMillis());
            log.info("成功处理案件状态事件");

        } catch (UnexpectedMessageTypeException | MessageDeserializationException e) {
            handleUnrecoverableError(channel, tag, e);
        } catch (Exception e) {
            handleRecoverableError(channel, tag, e);
        } finally {
            MDC.clear(); // 清除日志上下文
        }
    }

    /**
     * 从消息头提取消息ID
     */
    private String extractMessageId(Message message) {
        Object idHeader = message.getMessageProperties()
                .getHeaders()
                .get(RabbitConstants.MESSAGE_ID_HEADER);

        if (idHeader == null) {
            throw new IllegalStateException("消息头缺少messageId");
        }
        return idHeader.toString();
    }

    /**
     * 验证消息协议
     */
    private void validateMessageProtocol(Message message) {
        MessageType actualType = extractMessageType(message);
        if (!MessageType.CASE_STATUS_UPDATE.equals(actualType)) {
            throw new UnexpectedMessageTypeException(
                    "Expected CASE_STATUS_UPDATE but received: " + actualType
            );
        }
    }

    /**
     * 从消息头提取消息类型
     */
    private MessageType extractMessageType(Message message) {
        Object typeHeader = message.getMessageProperties()
                .getHeaders()
                .get(RabbitConstants.MESSAGE_TYPE_HEADER);

        if (typeHeader == null) {
            throw new UnexpectedMessageTypeException("消息头缺少类型标识");
        }

        try {
            return MessageType.fromCode(typeHeader.toString());
        } catch (IllegalArgumentException e) {
            throw new UnexpectedMessageTypeException("未知的消息类型: " + typeHeader);
        }
    }

    /**
     * 反序列化消息体
     */
    private CaseStatusEvent deserializeMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), CaseStatusEvent.class);
        } catch (IOException e) {
            throw new MessageDeserializationException("反序列化失败", e);
        }
    }

    /**
     * 处理业务逻辑
     */
    private void processBusinessLogic(CaseStatusEvent event) {
        log.info("处理案件状态变更: 案件ID={}, 新状态={}",
                event.getCaseId(), event.getToStatus());

        // 委托给业务服务处理
//        caseStatusService.processStatusChange(event);
    }

    /**
     * 确认消息
     */
    private void acknowledgeMessage(Channel channel, long tag) {
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            log.error("ACK操作失败", e);
            consumerMetrics.recordAckFailure();
        }
    }

    /**
     * 处理不可恢复错误
     */
    private void handleUnrecoverableError(
            Channel channel,
            long tag,
            Exception cause
    ) {
        log.error("不可恢复错误", cause);
        try {
            // 拒绝并不重新入队（进入死信队列）
            channel.basicReject(tag, false);
        } catch (IOException e) {
            log.error("Reject操作失败", e);
            consumerMetrics.recordRejectFailure();
        }
        consumerMetrics.recordUnrecoverableError(cause.getClass().getSimpleName());
    }

    /**
     * 处理可恢复错误
     */
    private void handleRecoverableError(
            Channel channel,
            long tag,
            Exception cause
    ) {
        log.error("可恢复错误，消息将重试", cause);
        try {
            // 拒绝并重新入队
            channel.basicNack(tag, false, true);
        } catch (IOException e) {
            log.error("NACK操作失败", e);
            consumerMetrics.recordNackFailure();
        }
        consumerMetrics.recordRecoverableError(cause.getClass().getSimpleName());
    }

    // 其他方法保持不变...
//    private void processActions(CaseStatusEvent event) {
//        for (ActionDefinition action : event.getPendingActions()) {
//            switch (action.getType()) {
//                case "CREATE_TASK":
//                    taskService.createTask(action);
//                    break;
//                case "SET_DEADLINE":
//                    deadlineService.setDeadline(action);
//                    break;
//                case "SEND_NOTIFICATION":
//                    notificationService.sendNotification(action);
//                    break;
//            }
//        }
//    }

}
