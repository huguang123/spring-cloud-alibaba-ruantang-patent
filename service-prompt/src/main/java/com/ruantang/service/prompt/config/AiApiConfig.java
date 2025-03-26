package com.ruantang.service.prompt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI接口配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai.api")
public class AiApiConfig {
    
    /**
     * API基础URL
     */
    private String baseUrl;
    
    /**
     * API密钥
     */
    private String apiKey;
} 