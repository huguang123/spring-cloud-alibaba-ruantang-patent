package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 操作权限更新请求
 */
@Data
@ApiModel(description = "操作权限更新请求")
public class PermUpdateRequest {
    
    @NotNull(message = "权限ID不能为空")
    @ApiModelProperty(value = "权限ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "权限名称")
    private String permsName;
    
    @ApiModelProperty(value = "权限编码")
    private String permsCode;

    @ApiModelProperty(value = "权限类型", required = true)
    private String permType;
    
    @ApiModelProperty(value = "HTTP方法(GET、POST、PUT、DELETE)")
    private String apiMethod;
    
    @ApiModelProperty(value = "接口路径")
    private String apiPath;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
} 