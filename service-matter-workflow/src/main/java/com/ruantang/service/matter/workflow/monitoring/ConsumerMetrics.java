package com.ruantang.service.matter.workflow.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConsumerMetrics {

    private final MeterRegistry meterRegistry;

    public void recordSuccess(long processingTimeMs) {
        meterRegistry.counter("case_status.consumer.success").increment();
        meterRegistry.timer("case_status.processing.time")
                .record(processingTimeMs, TimeUnit.MILLISECONDS);
    }

    public void recordIdempotentSkip() {
        meterRegistry.counter("case_status.consumer.idempotent_skip").increment();
    }

    public void recordUnrecoverableError(String errorType) {
        meterRegistry.counter("case_status.consumer.error.unrecoverable", "type", errorType).increment();
    }

    public void recordRecoverableError(String errorType) {
        meterRegistry.counter("case_status.consumer.error.recoverable", "type", errorType).increment();
    }

    public void recordAckFailure() {
        meterRegistry.counter("case_status.consumer.ack_failure").increment();
    }

    public void recordRejectFailure() {
        meterRegistry.counter("case_status.consumer.reject_failure").increment();
    }

    public void recordNackFailure() {
        meterRegistry.counter("case_status.consumer.nack_failure").increment();
    }
}
