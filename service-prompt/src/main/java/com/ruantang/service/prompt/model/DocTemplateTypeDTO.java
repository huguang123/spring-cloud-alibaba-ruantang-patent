package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档模板类型DTO
 */
@Data
public class DocTemplateTypeDTO {
    
    /**
     * 主键
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