package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档分项模板DTO
 */
@Data
public class DocSectionTemplateDTO {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 关联文档模板ID
     */
    private Long docTemplateId;
    
    /**
     * 关联提示词模板ID
     */
    private Long promptId;
    
    /**
     * 分项名称
     */
    private String sectionName;
    
    /**
     * 显示顺序
     */
    private Integer sortOrder;
} 