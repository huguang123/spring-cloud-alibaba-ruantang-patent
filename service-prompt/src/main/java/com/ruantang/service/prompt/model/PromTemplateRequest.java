package com.ruantang.service.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * 提示词模板请求DTO
 */
@Data
public class PromTemplateRequest {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 提示词模板类型
     */
    private String sectionType;
    
    /**
     * 提示词模板名称
     */
    private String promName;
    
    /**
     * 提示词模板内容
     */
    private String content;
    
    /**
     * 模板优先级
     */
    private Integer weight;
    
    /**
     * 模板版本
     */
    private String version;
    
    /**
     * 是否启用（true=启用，false=禁用）
     */
    private Boolean enabled;
    
    /**
     * 参数列表
     */
    private List<PromTemplateParameterRequest> parameters;
} 