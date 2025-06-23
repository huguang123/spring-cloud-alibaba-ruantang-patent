package com.ruantang.service.deadline.producer;

import com.ruantang.commons.message.MessageSender;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DeadlineMessageProducer {

    private static final String SOURCE_SERVICE = "service-deadline";

    @Resource
    private MessageSender messageSender;



}
