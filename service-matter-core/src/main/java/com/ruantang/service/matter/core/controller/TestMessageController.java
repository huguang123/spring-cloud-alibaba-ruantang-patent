package com.ruantang.service.matter.core.controller;

import com.ruantang.commons.enums.MessageType;
import com.ruantang.service.matter.core.event.CaseStatusEvent;
import com.ruantang.service.matter.core.producer.CoreMessageProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 消息流测试控制器
 * 注意：此接口仅用于开发环境测试，生产环境应禁用
 */
@RestController
@RequestMapping("/test/message")
public class TestMessageController {

    private final CoreMessageProducer coreMessageProducer;

    public TestMessageController(CoreMessageProducer coreMessageProducer) {
        this.coreMessageProducer = coreMessageProducer;
    }

    /**
     * 触发案件状态变更事件
     * @return 测试结果
     */
    @PostMapping("/trigger-case-status")
    public String triggerCaseStatusEvent() {
        // 创建测试事件
        CaseStatusEvent event = new CaseStatusEvent();
        event.setCaseId("TEST-CASE-" + UUID.randomUUID().toString().substring(0, 8));
        event.setFromStatus("DRAFT");
        event.setToStatus("SUBMITTED");
        event.setMessageType(MessageType.CASE_STATUS_UPDATE);
        event.setMessageId(UUID.randomUUID().toString());

        // 发送消息
        coreMessageProducer.sendCaseStatusEvent(event);

        return "案件状态事件已发送: " + event.getCaseId();
    }
}
