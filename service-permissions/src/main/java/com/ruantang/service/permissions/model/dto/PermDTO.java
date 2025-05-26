package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作权限DTO
 */
@Data
@ApiModel(description = "操作权限DTO")
public class PermDTO {
    
    @ApiModelProperty(value = "权限ID")
    private Long id;
    
    @ApiModelProperty(value = "权限编码")
    private String permsCode;

    @ApiModelProperty(value = "权限类型")
    private String permType;
    
    @ApiModelProperty(value = "权限名称")
    private String permsName;
    
    @ApiModelProperty(value = "HTTP方法")
    private String apiMethod;
    
    @ApiModelProperty(value = "接口路径")
    private String apiPath;
    
    @ApiModelProperty(value = "权限作用域")
    private String permScope;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 