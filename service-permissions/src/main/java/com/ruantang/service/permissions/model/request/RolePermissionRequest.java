package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色权限分配请求
 */
@Data
@ApiModel(description = "角色权限分配请求")
public class RolePermissionRequest {
    
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;
    
    @ApiModelProperty(value = "操作权限ID列表")
    private List<Long> permIds;
    
    @ApiModelProperty(value = "数据权限ID列表")
    private List<Long> dataPolicyIds;
} 