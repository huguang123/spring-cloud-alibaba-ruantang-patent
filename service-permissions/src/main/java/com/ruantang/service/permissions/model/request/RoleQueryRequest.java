package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色查询请求
 */
@Data
@ApiModel(description = "角色查询请求")
public class RoleQueryRequest {
    
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
    
    @ApiModelProperty(value = "角色类型(1:平台角色 2:企业角色 3:代理所角色)")
    private Integer roleType;
    
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页大小", required = true)
    private Integer pageSize = 10;
} 