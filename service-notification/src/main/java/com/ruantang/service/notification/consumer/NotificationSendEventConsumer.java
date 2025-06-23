
// NotificationSendEventConsumer.java
package com.ruantang.service.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.commons.enums.MessageType;
import com.ruantang.commons.exception.message.MessageDeserializationException;
import com.ruantang.commons.exception.message.UnexpectedMessageTypeException;
import com.ruantang.commons.service.IdempotencyService;
import com.ruantang.service.notification.event.NotificationSendEvent;
//import com.ruantang.service.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 通知发送事件消费者 - 处理来自工作流服务的通知发送事件
 *
 * <p>遵循行业最佳实践：
 * 1. 使用手动ACK模式确保消息可靠处理
 * 2. 实现幂等性检查防止重复消费
 * 3. 区分可恢复和不可恢复错误
 * 4. 详细日志记录关键处理步骤
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSendEventConsumer {

    private final ObjectMapper objectMapper;
    private final IdempotencyService idempotencyService;
//    private final NotificationService notificationService;

    /**
     * 消费通知发送事件
     *
     * @param message RabbitMQ消息对象
     * @param channel RabbitMQ通道
     * @param tag 消息投递标签
     */
    @RabbitListener(
            queues = RabbitConstants.NOTIFICATION_SEND_QUEUE,
            ackMode = "MANUAL"
    )
    public void consumeNotificationSendEvent(
            Message message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {
        final String messageId = getMessageId(message);
        MDC.put("messageId", messageId); // 日志上下文

        try {
            // 1. 消息协议验证
            validateMessageProtocol(message);

            // 2. 反序列化消息体
            NotificationSendEvent event = deserializeMessage(message);

            // 3. 幂等性检查
            if (idempotencyService.isProcessed(messageId)) {
                log.debug("消息已处理，跳过");
                acknowledgeMessage(channel, tag);
                return;
            }

            // 4. 处理业务逻辑
            processBusinessLogic(event);

            // 5. 标记为已处理
            idempotencyService.markAsProcessed(messageId);
            acknowledgeMessage(channel, tag);
            log.info("成功处理通知发送事件");

        } catch (UnexpectedMessageTypeException | MessageDeserializationException e) {
            handleUnrecoverableError(channel, tag, e);
        } catch (Exception e) {
            handleRecoverableError(channel, tag, e);
        } finally {
            MDC.clear(); // 清除日志上下文
        }
    }

    /**
     * 验证消息协议
     */
    private void validateMessageProtocol(Message message) {
        MessageType actualType = extractMessageType(message);
        if (!MessageType.NOTIFICATION_SENT.equals(actualType)) {
            throw new UnexpectedMessageTypeException(
                    "Expected NOTIFICATION_SENT but received: " + actualType
            );
        }
    }

    /**
     * 从消息头提取消息ID
     */
    private String getMessageId(Message message) {
        Object idHeader = message.getMessageProperties()
                .getHeaders()
                .get(RabbitConstants.MESSAGE_ID_HEADER);

        if (idHeader == null) {
            throw new IllegalStateException("消息头缺少messageId");
        }
        return idHeader.toString();
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
    private NotificationSendEvent deserializeMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), NotificationSendEvent.class);
        } catch (IOException e) {
            throw new MessageDeserializationException("反序列化失败", e);
        }
    }

    /**
     * 处理业务逻辑 - 发送通知
     */
    private void processBusinessLogic(NotificationSendEvent event) {
        log.info("发送通知 - 接收人: {}, 通知类型: {}, 内容: {}",
                event.getRecipient(), event.getNotificationType(), event.getContent());

//        notificationService.sendNotification(
//                event.getRecipient(),
//                event.getNotificationType(),
//                event.getContent(),
//                event.getParameters()
//        );
    }

    /**
     * 确认消息
     */
    private void acknowledgeMessage(Channel channel, long tag) {
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            log.error("ACK操作失败", e);
            // 触发监控告警
//            monitoringService.triggerAckFailureAlert();
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
        }
        // 触发监控告警
//        monitoringService.recordUnrecoverableError(cause);
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
        }
        // 记录错误指标
//        monitoringService.recordRecoverableError(cause);
    }
}