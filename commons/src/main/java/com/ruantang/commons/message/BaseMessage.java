package com.ruantang.commons.message;

import com.ruantang.commons.enums.MessageType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

/**
 * 传输层元数据（消息头）
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "事件消息基础对象", description = "事件消息")
public abstract class BaseMessage {

    private String messageId = UUID.randomUUID().toString(); // UUID唯一标识
    private String correlationId; // 关联ID用于跟踪
    private long timestamp = Instant.now().toEpochMilli(); // 消息创建时间戳
//    private String sourceService; // 来源服务
    private MessageType messageType; // 消息类型
    private int version; // 消息版本
    private boolean encrypted; // 是否加密
    private int priority; // 消息优先级
}
