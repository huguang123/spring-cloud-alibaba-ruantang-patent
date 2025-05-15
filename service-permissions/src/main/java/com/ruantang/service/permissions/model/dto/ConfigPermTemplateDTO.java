package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配置权限模板DTO
 */
@Data
@ApiModel(description = "配置权限模板DTO")
public class ConfigPermTemplateDTO {
    
    @ApiModelProperty(value = "模板ID")
    private Long id;
    
    @ApiModelProperty(value = "模板编码")
    private String templateCode;
    
    @ApiModelProperty(value = "模板类型(1:平台配置模板 2:企业租户配置模板 3:代理所配置模板)")
    private Integer templateType;
    
    @ApiModelProperty(value = "模板类型名称")
    private String templateTypeName;
    
    @ApiModelProperty(value = "基础描述")
    private String baseDescription;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 