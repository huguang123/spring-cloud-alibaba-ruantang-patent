package com.ruantang.service.task.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TaskMetrics {

    private final MeterRegistry meterRegistry;

    public TaskMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordConsumptionSuccess(String eventType) {
        meterRegistry.counter("task.consumer.success", "event", eventType).increment();
    }

    public void recordConsumptionFailure(String eventType, String errorCode) {
        meterRegistry.counter("task.consumer.failure",
                "event", eventType,
                "code", errorCode
        ).increment();
    }

    public void recordProcessingTime(String eventType, long milliseconds) {
        meterRegistry.timer("task.processing.time", "event", eventType)
                .record(milliseconds, TimeUnit.MILLISECONDS);
    }
}
