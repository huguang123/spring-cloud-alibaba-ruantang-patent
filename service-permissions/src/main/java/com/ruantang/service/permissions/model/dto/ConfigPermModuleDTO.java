package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配置权限模块DTO
 */
@Data
@ApiModel(description = "配置权限模块DTO")
public class ConfigPermModuleDTO {
    
    @ApiModelProperty(value = "模块ID")
    private Long id;
    
    @ApiModelProperty(value = "模块名称")
    private String moduleName;
    
    @ApiModelProperty(value = "模板版本ID")
    private Long templateVersionId;
    
    @ApiModelProperty(value = "模块类型(1=功能权限模块 2=数据权限模块)")
    private Integer moduleType;
    
    @ApiModelProperty(value = "模块类型名称")
    private String moduleTypeName;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 