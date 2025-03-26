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

    /**
     * 调用AI接口生成内容
     *
     * @param prompt 提示词
     * @return 生成的内容
     */
    public String generateContent(String prompt) {
        log.info("调用AI接口，提示词：{}", prompt);
        String url = aiApiConfig.getBaseUrl() + "/v1/chat/completions";
        
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiConfig.getApiKey());
        
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