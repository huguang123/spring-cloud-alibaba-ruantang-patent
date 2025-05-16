package com.ruantang.service.tenant.model.dto;

import lombok.Data;

/**
 * 租户模板角色关联DTO
 */
@Data
public class TenantTemplateRoleDTO {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 角色信息
     */
    private SysRolesDTO role;
    
    /**
     * 是否继承权限变更
     */
    private Integer isInherit;
    
    /**
     * 权限快照(创建时JSON结构)
     * 如果继承权限变更，这里为空，如果不继承权限变更，需要存储权限快照
     */
    private String permissionSnapshot;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 