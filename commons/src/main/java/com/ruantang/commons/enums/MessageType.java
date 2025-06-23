package com.ruantang.commons.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    // 核心服务事件
    CASE_STATUS_UPDATE("CASE_STATUS_UPDATE", "案件状态更新事件"),

    // 工作流服务事件
    TASK_CREATED("TASK_CREATED", "任务创建事件"),
    NOTIFICATION_SENT("NOTIFICATION_SENT", "通知发送事件"),
    DEADLINE_SET("DEADLINE_SET", "期限设置事件"),

    // 其他服务事件
    PATENT_EXPIRATION_WARNING("PATENT_EXPIRATION_WARNING", "专利到期预警事件"),

    // 系统级事件
    SYSTEM_ALERT("SYSTEM_ALERT", "系统告警事件");

    private final String code;
    private final String description;

    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     */
    public static MessageType fromCode(String code) {
        for (MessageType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知消息类型编码: " + code);
    }
}
