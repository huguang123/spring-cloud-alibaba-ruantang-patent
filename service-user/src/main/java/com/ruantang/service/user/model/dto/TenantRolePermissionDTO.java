package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * A租户角色权限DTO
 */
@Data
@ApiModel(description = "租户角色权限DTO")
public class TenantRolePermissionDTO {
    
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    
    /**
     * 模板ID
     */
    @ApiModelProperty(value = "模板ID")
    private Long templateId;
    
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    
    /**
     * 是否继承权限变更(0:否 1:是)
     */
    @ApiModelProperty(value = "是否继承权限变更(0:否 1:是)")
    private Integer isInherit;
    
    /**
     * 权限快照，当isInherit=0时有值
     */
    @ApiModelProperty(value = "权限快照，当isInherit=0时有值")
    private String permissionSnapshot;
    
    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String rolesCode;
    
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String rolesName;
} 