package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板版本配置DTO
 */
@Data
@ApiModel(description = "模板版本配置DTO")
public class ConfigPermVersionDTO {
    
    @ApiModelProperty(value = "版本ID")
    private Long id;
    
    @ApiModelProperty(value = "关联模板ID")
    private Long templateId;
    
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    
    @ApiModelProperty(value = "语义化版本号")
    private String version;
    
    @ApiModelProperty(value = "是否默认版本(0:否 1:是)")
    private Integer isDefault;
    
    @ApiModelProperty(value = "版本描述")
    private String versionDescription;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 