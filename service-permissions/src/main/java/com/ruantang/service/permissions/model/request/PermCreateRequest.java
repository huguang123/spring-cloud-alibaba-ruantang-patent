package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 操作权限创建请求
 */
@Data
@ApiModel(description = "操作权限创建请求")
public class PermCreateRequest {
    
    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty(value = "权限名称", required = true)
    private String permsName;
    
    @NotBlank(message = "权限编码不能为空")
    @ApiModelProperty(value = "权限编码", required = true)
    private String permsCode;

    @NotBlank(message = "权限类型不能为空")
    @ApiModelProperty(value = "权限类型", required = true)
    private String permType;
    
    @ApiModelProperty(value = "HTTP方法(GET、POST、PUT、DELETE)")
    private String apiMethod;
    
    @ApiModelProperty(value = "接口路径")
    private String apiPath;
    
    @ApiModelProperty(value = "权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）")
    private String permScope;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
} 