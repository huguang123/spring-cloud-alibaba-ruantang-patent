package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 版本更新请求
 */
@Data
@ApiModel(description = "版本更新请求")
public class VersionUpdateRequest {
    
    @NotNull(message = "版本ID不能为空")
    @ApiModelProperty(value = "版本ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "语义化版本号")
    private String version;
    
    @ApiModelProperty(value = "是否默认版本(0:否 1:是)")
    private Integer isDefault;
    
    @ApiModelProperty(value = "版本描述")
    private String versionDescription;
} 