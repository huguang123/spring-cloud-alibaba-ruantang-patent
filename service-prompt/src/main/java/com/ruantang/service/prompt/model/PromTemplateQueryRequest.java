package com.ruantang.service.prompt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 提示词模板查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PromTemplateQueryRequest extends PageRequest {
    
    /**
     * 模板类型
     */
    private String sectionType;
    
    /**
     * 模板名称（模糊查询）
     */
    private String promName;
    
    /**
     * 模板版本
     */
    private String version;
    
    /**
     * 是否启用（true=启用，false=禁用）
     */
    private Boolean enabled;
} 