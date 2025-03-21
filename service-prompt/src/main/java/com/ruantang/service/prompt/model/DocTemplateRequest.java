package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档模板请求DTO
 */
@Data
public class DocTemplateRequest {
    
    /**
     * 主键（新增时为空）
     */
    private Long id;
    
    /**
     * 关联文档模板分类ID
     */
    private Long templateTypeId;
    
    /**
     * 关联技术领域ID（NULL表示通用模板）
     */
    private Long domainId;
    
    /**
     * 关联组织表ID
     */
    private Long orgId;
    
    /**
     * 模板名称
     */
    private String templateName;
} 