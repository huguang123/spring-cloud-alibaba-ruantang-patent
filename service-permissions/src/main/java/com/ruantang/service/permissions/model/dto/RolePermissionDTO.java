package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色权限关联DTO
 */
@Data
@ApiModel(description = "角色权限关联DTO")
public class RolePermissionDTO {
    
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
    
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    
    @ApiModelProperty(value = "已分配的操作权限ID列表")
    private List<Long> permIds;
    
    @ApiModelProperty(value = "已分配的数据权限ID列表")
    private List<Long> dataPolicyIds;
} 