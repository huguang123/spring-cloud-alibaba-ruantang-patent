package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 提示词模板参数请求DTO
 */
@Data
public class PromTemplateParameterRequest {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 关联模板表ID
     */
    private Long templateId;
    
    /**
     * 参数键
     */
    private String paramKey;
    
    /**
     * 标识模板类型（0-系统提示词或1-用户提示词）
     */
    private Integer promRole;
    
    /**
     * 参数默认值
     */
    private String defaultValue;
    
    /**
     * 输入提醒
     */
    private String enterReminder;
    
    /**
     * 变量描述
     */
    private String description;
} 