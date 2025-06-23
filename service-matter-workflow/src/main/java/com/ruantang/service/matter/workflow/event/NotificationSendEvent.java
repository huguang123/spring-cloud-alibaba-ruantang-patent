package com.ruantang.service.matter.workflow.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "通知发送事件")
public class NotificationSendEvent extends BaseMessage {


}
