package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色DTO
 */
@Data
@ApiModel(description = "角色DTO")
public class SysRolesDTO {
    
    @ApiModelProperty(value = "角色ID")
    private Long id;
    
    @ApiModelProperty(value = "角色编码")
    private String rolesCode;
    
    @ApiModelProperty(value = "角色名称")
    private String rolesName;
    
    @ApiModelProperty(value = "角色类型(1:平台角色 2:企业角色 3:代理所角色)")
    private Integer rolesType;
    
    @ApiModelProperty(value = "角色类型名称")
    private String rolesTypeName;
    
    @ApiModelProperty(value = "角色描述")
    private String rolesDescription;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 