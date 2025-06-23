package com.ruantang.service.matter.workflow.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@ApiModel(description = "任务创建事件")
public class TaskCreateEvent extends BaseMessage {

    private String caseId;
    private String taskType;
    private String assigneeId;
    private ZonedDateTime dueDate;
    private Map<String, Object> parameters;

}
