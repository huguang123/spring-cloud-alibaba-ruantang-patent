package com.ruantang.service.tenant.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板角色DTO，包含角色信息和模板角色关联信息
 */
@Data
@ApiModel(description = "模板角色DTO")
public class TemplateRoleDTO {
    
    /**
     * 关联ID
     */
    @ApiModelProperty(value = "关联ID")
    private Long id;
    
    /**
     * 模板ID
     */
    @ApiModelProperty(value = "模板ID")
    private Long templateId;
    
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    
    /**
     * 是否继承权限变更
     */
    @ApiModelProperty(value = "是否继承权限变更(0:否 1:是)")
    private Integer isInherit;
    
    /**
     * 权限快照（暴露权限快照，危险行为，禁止）
     */
//    @ApiModelProperty(value = "权限快照")
//    private String permissionSnapshot;
    
    /**
     * 角色信息
     */
    @ApiModelProperty(value = "角色信息")
    private SysRolesDTO role;
} 