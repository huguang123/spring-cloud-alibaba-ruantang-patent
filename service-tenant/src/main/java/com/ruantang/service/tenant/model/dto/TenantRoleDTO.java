package com.ruantang.service.tenant.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户角色DTO
 */
@Data
@ApiModel(description = "租户角色DTO")
public class TenantRoleDTO {
    
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    
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
    
    /**
     * 角色类型(1:平台角色 2:企业角色 3:代理所角色)
     */
    @ApiModelProperty(value = "角色类型(1:平台角色 2:企业角色 3:代理所角色)")
    private Integer rolesType;
    
    /**
     * 角色类型名称
     */
    @ApiModelProperty(value = "角色类型名称")
    private String rolesTypeName;
    
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String rolesDescription;
    
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
     * 是否继承权限变更
     */
    @ApiModelProperty(value = "是否继承权限变更(0:否 1:是)")
    private Integer isInherit;
} 