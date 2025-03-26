package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档生成请求DTO
 */
@Data
public class DocGenerateRequest {
    
    /**
     * 技术领域ID
     */
    private Long techDomainId;
    
    /**
     * 文档模板ID
     */
    private Long docTemplateId;
    
    /**
     * 技术概述（用户输入）
     */
    private String techDescription;
    
    /**
     * 是否使用AI辅助生成
     */
    private Boolean useAiGenerate;
} 