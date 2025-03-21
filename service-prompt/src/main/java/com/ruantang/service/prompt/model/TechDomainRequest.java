package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 技术领域请求DTO类
 */
@Data
public class TechDomainRequest {
    
    /**
     * 主键（新增时为空）
     */
    private Long id;
    
    /**
     * 领域名称
     */
    private String domainName;
    
    /**
     * 父级领域ID（一级领域为null）
     */
    private Long parentId;
    
    /**
     * 领域描述
     */
    private String description;
} 