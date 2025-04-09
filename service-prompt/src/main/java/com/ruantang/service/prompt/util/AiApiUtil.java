package com.ruantang.service.prompt.util;

import com.ruantang.service.prompt.config.AiApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AI接口调用工具类
 */
@Slf4j
@Component
public class AiApiUtil {

    @Autowired
    private AiApiConfig aiApiConfig;
    
    @Autowired
    private RestTemplate restTemplate;

    private final AtomicInteger currentApiIndex = new AtomicInteger(0); // 当前使用的API索引

    /**
     * 调用AI接口生成内容
     *
     * @param prompt 提示词
     * @return 生成的内容
     */
    public String generateContent(String prompt) {
        log.info("调用AI接口，提示词：{}，ai接口api标号：{}", prompt, currentApiIndex.get());

        List<AiApiConfig.AiApi> apis = aiApiConfig.getApis();
        if (apis == null || apis.isEmpty()) {
            log.error("未配置有效的AI API");
            return null;
        }

        int apiCount = apis.size();
        for (int i = 0; i < apiCount; i++) {
            int currentIndex = currentApiIndex.getAndIncrement(); // 获取当前索引并递增
            int index = currentIndex % apiCount; // 计算实际索引（防止越界）
            AiApiConfig.AiApi api = apis.get(index);

            try {
                String content = callAiApi(api, prompt);
                if (content != null) {
                    return content;
                }
            } catch (Exception e) {
                log.error("调用AI接口[{}]失败", api.getBaseUrl(), e);
            }

            // 切换到下一个API
            currentApiIndex.set((currentIndex + 1) % apiCount); // 更新索引，确保循环
        }

        log.error("所有AI接口均调用失败");
        return null;
    }

    /**
     * 调用单个AI API
     *
     * @param api    AI API 配置
     * @param prompt 提示词
     * @return 生成的内容
     */
    private String callAiApi(AiApiConfig.AiApi api, String prompt) {
        String url = api.getBaseUrl() + "/v1/chat/completions";

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(api.getApiKey());

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });

        // 发送请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 解析响应
                Map responseBody = response.getBody();
                log.info("调用AI接口成功，响应结果：{}", responseBody);
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> choice = choices.get(0);
                        Map<String, String> message = (Map<String, String>) choice.get("message");
                        return message.get("content");
                    }
                }
            }
        } catch (Exception e) {
            log.error("调用AI接口失败", e);
        }

        return null;
    }
} 