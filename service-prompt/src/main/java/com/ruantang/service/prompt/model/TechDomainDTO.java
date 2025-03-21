package com.ruantang.service.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * 技术领域DTO类
 */
@Data
public class TechDomainDTO {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 领域名称
     */
    private String domainName;
    
    /**
     * 父级领域ID
     */
    private Long parentId;
    
    /**
     * 父级领域名称
     */
    private String parentName;
    
    /**
     * 层级深度
     */
    private Integer level;
    
    /**
     * 领域描述
     */
    private String description;
    
    /**
     * 子领域列表
     */
    private List<TechDomainDTO> children;

    /**
     * 父级技术领域
     */
    private TechDomainDTO parent;
} 