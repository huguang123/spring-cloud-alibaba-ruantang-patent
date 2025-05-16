package com.ruantang.service.tenant.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色权限DTO
 */
@Data
public class RolePermissionDTO {
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 操作权限ID列表
     */
    private List<Long> permIds;
    
    /**
     * 数据权限策略ID列表
     */
    private List<Long> dataPolicyIds;
} 