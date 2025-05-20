package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 操作权限更新请求
 */
@Data
@ApiModel(value = "操作权限更新请求", description = "操作权限更新请求参数")
public class PermUpdateRequest {
    
    @NotNull(message = "权限ID不能为空")
    @ApiModelProperty(value = "权限ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "权限标识(如order:view)")
    private String permsCode;
    
    @ApiModelProperty(value = "权限名称")
    private String permsName;
    
    @ApiModelProperty(value = "HTTP方法(GET、POST、PUT、DELETE)")
    private String apiMethod;
    
    @ApiModelProperty(value = "接口路径")
    private String apiPath;
    
    @ApiModelProperty(value = "权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）")
    private String permScope;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
} 