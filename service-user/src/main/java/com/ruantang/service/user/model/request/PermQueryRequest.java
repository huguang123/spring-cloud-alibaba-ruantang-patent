package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作权限查询请求
 */
@Data
@ApiModel(value = "操作权限查询请求", description = "操作权限查询请求参数")
public class PermQueryRequest {
    
    @ApiModelProperty(value = "权限标识(支持模糊查询)")
    private String permsCode;
    
    @ApiModelProperty(value = "权限名称(支持模糊查询)")
    private String permsName;
    
    @ApiModelProperty(value = "权限作用域")
    private String permScope;
    
    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;
} 