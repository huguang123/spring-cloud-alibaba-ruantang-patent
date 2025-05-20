package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 操作权限创建请求
 */
@Data
@ApiModel(value = "操作权限创建请求", description = "操作权限创建请求参数")
public class PermCreateRequest {
    
    @NotBlank(message = "权限标识不能为空")
    @ApiModelProperty(value = "权限标识(如order:view)", required = true)
    private String permsCode;
    
    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty(value = "权限名称", required = true)
    private String permsName;
    
    @ApiModelProperty(value = "HTTP方法(GET、POST、PUT、DELETE)")
    private String apiMethod;
    
    @ApiModelProperty(value = "接口路径")
    private String apiPath;
    
    @NotBlank(message = "权限作用域不能为空")
    @ApiModelProperty(value = "权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）", required = true)
    private String permScope;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
} 