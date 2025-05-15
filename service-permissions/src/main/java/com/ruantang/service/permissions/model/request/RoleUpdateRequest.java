package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色更新请求
 */
@Data
@ApiModel(description = "角色更新请求")
public class RoleUpdateRequest {
    
    @ApiModelProperty(value = "角色ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "角色名称")
    private String rolesName;
    
    @ApiModelProperty(value = "角色描述")
    private String rolesDescription;
} 