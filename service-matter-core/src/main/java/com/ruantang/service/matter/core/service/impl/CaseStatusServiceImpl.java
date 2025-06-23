package com.ruantang.service.matter.core.service.impl;

import com.ruantang.service.matter.core.event.CaseStatusEvent;
import com.ruantang.service.matter.core.producer.CoreMessageProducer;
import com.ruantang.service.matter.core.service.CaseStatusService;
import jakarta.annotation.Resource;

public class CaseStatusServiceImpl implements CaseStatusService {

    @Resource
    private CoreMessageProducer messageProducer;
    @Override
    public void updateCaseStatus(String caseId, String currentStatus, String patentType) {
        // 1. 同步调用Workflow获取迁移结果
//        TransitionResult result = workflowClient.calculateTransition(caseId, currentStatus, patentType);

        // 2. 验证迁移合法性
//        if (!isValidTransition(result)) {
//            throw new IllegalStateException("Invalid state transition");
//        }

        // 3. 更新状态
//        PatentCase patentCase = caseRepository.findById(caseId)
//                .orElseThrow(() -> new CaseNotFoundException(caseId));
//        patentCase.applyTransition(result);
//        caseRepository.save(patentCase);

        // 4. 发布状态变更事件
//        CaseStatusEvent statusEvent = createStatusEvent(caseId, result);
//        messageProducer.sendCaseStatusEvent(statusEvent);
    }

//    private CaseStatusEvent createStatusEvent(String caseId, TransitionResult result) {
//        CaseStatusEvent event = new CaseStatusEvent();
//        event.setEventId(UUID.randomUUID());
//        event.setCorrelationId(UUID.randomUUID());
//        event.setTimestamp(Instant.now());
//        event.setSourceService("matter.core");
//        event.setVersion("1.0");
//        event.setCaseId(caseId);
//        event.setFromStatus(result.getOldStatus());
//        event.setToStatus(result.getNewStatus());
//        event.setPendingActions(result.getActions());
//        return event;
//    }
}
