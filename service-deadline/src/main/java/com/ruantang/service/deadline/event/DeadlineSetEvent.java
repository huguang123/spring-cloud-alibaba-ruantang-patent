package com.ruantang.service.deadline.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@ApiModel(description = "期限设置事件事件")
public class DeadlineSetEvent extends BaseMessage {

    private String caseId;
    private String deadlineType;
    private ZonedDateTime dueDate;
    private Map<String, Object> parameters;
}
