package com.ruantang.service.tenant.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 租户模板DTO
 */
@Data
public class TenantTemplateDTO {
    
    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 模板编码(如: EDU_TEMPLATE)
     */
    private String templateCode;
    
    /**
     * 模板名称(如: 教育行业模板)
     */
    private String templateName;
    
    /**
     * 模板类型（1：平台租户模板、2：企业租户模板、3：代理所租户模板）
     */
    private Integer templateType;
    
    /**
     * 模板类型名称
     */
    private String templateTypeName;
    
    /**
     * 行业类型(0:教育 1:医疗 2:金融)
     */
    private Integer industryType;
    
    /**
     * 行业类型名称
     */
    private String industryTypeName;
    
    /**
     * 数据隔离模式(继承租户配置)
     */
    private Integer dataIsolationMode;
    
    /**
     * 是否系统内置模板
     */
    private Integer isSystem;

    /**
     * 模板描述
     */
    private String templateDesc;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
    
    /**
     * 关联角色列表
     */
    private List<TenantTemplateRoleDTO> roles;
} 