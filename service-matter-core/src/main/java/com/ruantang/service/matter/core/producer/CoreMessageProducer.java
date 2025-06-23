package com.ruantang.service.matter.core.producer;

import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.commons.message.MessageSender;
import com.ruantang.service.matter.core.event.CaseStatusEvent;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CoreMessageProducer {

    private static final String SOURCE_SERVICE = "service-matter-core";

    @Resource
    private MessageSender messageSender;

    /**
     * 发送案件状态变更事件
     * @param event 案件状态变更事件
     */
    public void sendCaseStatusEvent(CaseStatusEvent event) {
        messageSender.sendMessage(
                event,
                RabbitConstants.STATUS_EXCHANGE,
                RabbitConstants.STATUS_ROUTING_KEY,
                SOURCE_SERVICE
        );
    }

}
