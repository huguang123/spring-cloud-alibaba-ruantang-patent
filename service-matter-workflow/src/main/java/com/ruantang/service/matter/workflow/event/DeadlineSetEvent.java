package com.ruantang.service.matter.workflow.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "期限设置事件事件")
public class DeadlineSetEvent extends BaseMessage {
}
