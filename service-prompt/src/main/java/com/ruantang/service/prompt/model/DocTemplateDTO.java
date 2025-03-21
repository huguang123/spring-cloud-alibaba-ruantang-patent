package com.ruantang.service.prompt.model;

import lombok.Data;
import java.util.List;

/**
 * 文档模板DTO
 */
@Data
public class DocTemplateDTO {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 关联文档模板分类ID
     */
    private Long templateTypeId;
    
    /**
     * 模板类型名称
     */
    private String templateTypeName;
    
    /**
     * 关联技术领域ID
     */
    private Long domainId;
    
    /**
     * 技术领域名称
     */
    private String domainName;
    
    /**
     * 关联组织表ID
     */
    private Long orgId;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 文档分项模板列表
     */
    private List<DocSectionTemplateDTO> sections;
} 