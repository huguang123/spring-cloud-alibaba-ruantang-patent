package com.ruantang.service.matter.workflow.producer;

import com.ruantang.commons.constants.RabbitConstants;
import com.ruantang.commons.message.MessageSender;
import com.ruantang.service.matter.workflow.event.DeadlineSetEvent;
import com.ruantang.service.matter.workflow.event.NotificationSendEvent;
import com.ruantang.service.matter.workflow.event.TaskCreateEvent;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WorkflowMessageProducer {

    private static final String SOURCE_SERVICE = "service-matter-workflow";

    @Resource
    private MessageSender messageSender;

    /**
     * 发送任务创建事件
     * @param event 任务创建事件
     */
    public void sendTaskCreateEvent(TaskCreateEvent event) {
        messageSender.sendMessage(
                event,
                RabbitConstants.TASK_CREATE_EXCHANGE,
                RabbitConstants.TASK_CREATE_ROUTING_KEY, // 使用空路由键
                SOURCE_SERVICE
        );
    }

    /**
     * 发送通知事件
     * @param event 通知发送事件
     */
    public void sendNotificationEvent(NotificationSendEvent event) {
        messageSender.sendMessage(
                event,
                RabbitConstants.NOTIFICATION_SEND_EXCHANGE,
                RabbitConstants.NOTIFICATION_SEND_ROUTING_KEY,
                SOURCE_SERVICE
        );
    }

    /**
     * 发送期限设置事件
     * @param event 期限设置事件
     */
    public void sendDeadlineSetEvent(DeadlineSetEvent event) {
        messageSender.sendMessage(
                event,
                RabbitConstants.DEADLINE_SET_EXCHANGE,
                RabbitConstants.DEADLINE_SET_ROUTING_KEY,
                SOURCE_SERVICE
        );
    }




}
