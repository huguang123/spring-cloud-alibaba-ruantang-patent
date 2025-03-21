package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 文档分项模板请求DTO
 */
@Data
public class DocSectionTemplateRequest {
    
    /**
     * 主键（新增时为空）
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