package com.ruantang.service.prompt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

// 新建配置类 AiAsyncConfig.java
@Configuration
@EnableAsync
@Slf4j
public class AiAsyncConfig {
    /**
     * AI专用线程池
     */
    @Bean("aiExecutor")
    public Executor aiThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);    // 常规并发量
        executor.setMaxPoolSize(15);    // 突发流量上限
        executor.setQueueCapacity(50);  // 等待队列长度
        executor.setThreadNamePrefix("ai-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        executor.setTaskDecorator(runnable -> {
            log.debug("AI线程池活跃线程数：{}", executor.getActiveCount());
            return runnable;
        });
        return executor;
    }
}
