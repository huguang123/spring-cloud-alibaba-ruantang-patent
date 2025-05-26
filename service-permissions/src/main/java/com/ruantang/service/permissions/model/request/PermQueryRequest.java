package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作权限查询请求
 */
@Data
@ApiModel(description = "操作权限查询请求")
public class PermQueryRequest {
    
    @ApiModelProperty(value = "权限名称")
    private String permsName;
    
    @ApiModelProperty(value = "权限编码")
    private String permsCode;
    
    @ApiModelProperty(value = "HTTP方法(GET、POST、PUT、DELETE)")
    private String apiMethod;

    @ApiModelProperty(value = "权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）")
    private String permScope;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
} 