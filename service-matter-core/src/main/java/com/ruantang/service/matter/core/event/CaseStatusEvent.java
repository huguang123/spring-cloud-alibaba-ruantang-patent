package com.ruantang.service.matter.core.event;

import com.ruantang.commons.message.BaseMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "状态变更事件")
public class CaseStatusEvent extends BaseMessage {

    private String caseId;
    private String fromStatus;
    private String toStatus;

//    private List<ActionDefinition> pendingActions;

}
