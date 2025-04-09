package com.ruantang.service.prompt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * AI接口配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai.api")
public class AiApiConfig {

    /**
     * 多个API的基础URL和密钥列表
     */
    private List<AiApi> apis;

    /**
     * AI API 配置项
     */
    @Data
    public static class AiApi {
        /**
         * API基础URL
         */
        private String baseUrl;

        /**
         * API密钥
         */
        private String apiKey;
    }
} 