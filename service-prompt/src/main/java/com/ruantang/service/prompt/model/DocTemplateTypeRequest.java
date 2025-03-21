package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档模板类型请求DTO
 */
@Data
public class DocTemplateTypeRequest {
    
    /**
     * 主键（新增时为空）
     */
    private Long id;
    
    /**
     * 模板类型说明
     */
    private String typeName;
    
    /**
     * 模板类型描述
     */
    private String description;
} 