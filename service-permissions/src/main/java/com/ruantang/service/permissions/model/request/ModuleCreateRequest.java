package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模块创建请求
 */
@Data
@ApiModel(description = "模块创建请求")
public class ModuleCreateRequest {
    
    @NotBlank(message = "模块名称不能为空")
    @ApiModelProperty(value = "模块名称", required = true)
    private String moduleName;
    
    @NotNull(message = "模板版本ID不能为空")
    @ApiModelProperty(value = "模板版本ID", required = true)
    private Long templateVersionId;
    
    @NotNull(message = "模块类型不能为空")
    @ApiModelProperty(value = "模块类型(1=功能权限模块 2=数据权限模块)", required = true)
    private Integer moduleType;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort = 0;
} 