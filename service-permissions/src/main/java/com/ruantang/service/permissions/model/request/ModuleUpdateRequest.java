package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模块更新请求
 */
@Data
@ApiModel(description = "模块更新请求")
public class ModuleUpdateRequest {
    
    @NotNull(message = "模块ID不能为空")
    @ApiModelProperty(value = "模块ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "模块名称")
    private String moduleName;
    
    @ApiModelProperty(value = "模块类型(1=功能权限模块 2=数据权限模块)")
    private Integer moduleType;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort;
} 