package com.ruantang.service.deadline.consumer.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.service.deadline.event.DeadlineSetEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadlineDlqHandler {

    private final ObjectMapper objectMapper;
//    private final DlqRecordService dlqRecordService;

    @RabbitListener(queues = RabbitConstants.DEADLINE_DLQ_QUEUE)
    public void handleDeadlineDlq(Message failedMessage) {
        try {
            DeadlineSetEvent event = objectMapper.readValue(
                    failedMessage.getBody(),
                    DeadlineSetEvent.class
            );

            log.error("死信队列期限设置事件: {}", event);

            // 记录到数据库
//            dlqRecordService.recordFailedDeadline(
//                    event,
//                    failedMessage.getMessageProperties().getReceivedRoutingKey(),
//                    "DEADLINE_SET_FAILURE"
//            );

        } catch (Exception e) {
            log.error("处理死信队列消息失败", e);
            // 保存原始消息
//            dlqRecordService.saveRawMessage(failedMessage.getBody());
        }
    }
}