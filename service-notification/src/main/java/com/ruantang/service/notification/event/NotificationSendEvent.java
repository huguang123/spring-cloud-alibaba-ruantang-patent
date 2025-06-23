package com.ruantang.service.notification.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(description = "通知发送事件")
public class NotificationSendEvent extends BaseMessage {

    private String recipient;       // 接收人（用户ID、邮箱、手机号等）
    private String notificationType; // 通知类型（EMAIL, SMS, PUSH等）
    private String content;          // 通知内容
    private Map<String, Object> parameters; // 附加参数
}
