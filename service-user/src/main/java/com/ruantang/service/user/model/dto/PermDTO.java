package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作权限DTO
 */
@Data
@ApiModel(value = "操作权限DTO", description = "操作权限数据传输对象")
public class PermDTO {
    
    @ApiModelProperty(value = "主键ID")
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
    
    @ApiModelProperty(value = "权限类型(API:接口权限 BUTTON:按钮权限)")
    private String permType;
    
    @ApiModelProperty(value = "权限描述")
    private String permsDescription;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 