package com.ruantang.service.task.producer;

import com.ruantang.commons.message.MessageSender;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TaskMessageProducer {

    private static final String SOURCE_SERVICE = "service-task";

    @Resource
    private MessageSender messageSender;



}
