package com.ruantang.service.organization.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色创建请求
 */
@Data
@ApiModel(description = "角色创建请求")
public class RoleCreateRequest {
    
    @ApiModelProperty(value = "角色编码", required = true)
    private String rolesCode;
    
    @ApiModelProperty(value = "角色名称", required = true)
    private String rolesName;
    
    @ApiModelProperty(value = "角色类型(1:平台角色 2:企业角色 3:代理所角色)", required = true)
    private Integer rolesType;
    
    @ApiModelProperty(value = "角色描述")
    private String rolesDescription;
} 