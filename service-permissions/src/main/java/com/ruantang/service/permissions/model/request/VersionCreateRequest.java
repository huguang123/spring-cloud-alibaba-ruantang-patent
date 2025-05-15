package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 版本创建请求
 */
@Data
@ApiModel(description = "版本创建请求")
public class VersionCreateRequest {
    
    @NotNull(message = "模板ID不能为空")
    @ApiModelProperty(value = "模板ID", required = true)
    private Long templateId;
    
    @NotBlank(message = "版本号不能为空")
    @ApiModelProperty(value = "语义化版本号", required = true)
    private String version;
    
    @ApiModelProperty(value = "是否默认版本(0:否 1:是)")
    private Integer isDefault = 0;
    
    @ApiModelProperty(value = "版本描述")
    private String versionDescription;
} 