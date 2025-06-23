package com.ruantang.commons.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.commons.exception.message.MessageSerializationException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;

/**
 * 通用消息发送工具类
 */
@Component
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public MessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 通用消息发送方法
     *
     * @param event 消息事件对象
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param sourceService 来源服务标识
     */
    public void sendMessage(BaseMessage event, String exchange, String routingKey, String sourceService) {
        try {
            Message message = createRabbitMessage(event, sourceService);
            rabbitTemplate.send(exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            throw new MessageSerializationException("消息序列化失败: " + event, e);
        }
    }

    /**
     * 创建RabbitMQ消息对象
     */
    public Message createRabbitMessage(BaseMessage event, String sourceService)
            throws JsonProcessingException {
        // 创建消息属性
        MessageProperties props = createMessageProperties(event, sourceService);

        // 序列化消息体
        byte[] body = objectMapper.writeValueAsBytes(event);

        return new Message(body, props);
    }

    /**
     * 创建消息属性
     */
    private MessageProperties createMessageProperties(BaseMessage event, String sourceService) {
        MessageProperties props = new MessageProperties();
        props.setCorrelationId(event.getCorrelationId());
        props.setTimestamp(Date.from(Instant.ofEpochSecond(event.getTimestamp())));

        // 设置标准消息头
        props.setHeader(RabbitConstants.MESSAGE_ID_HEADER, event.getMessageId());
        props.setHeader(RabbitConstants.SOURCE_SERVICE_HEADER, sourceService);
        props.setHeader(RabbitConstants.MESSAGE_TYPE_HEADER, event.getMessageType().getCode());

        // 可选扩展头
        if (event.getVersion() > 0) {
            props.setHeader(RabbitConstants.VERSION_HEADER, event.getVersion());
        }
        if (event.getPriority() > 0) {
            props.setPriority(event.getPriority());
        }

        return props;
    }
}
