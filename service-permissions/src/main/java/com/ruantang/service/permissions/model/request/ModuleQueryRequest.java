package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模块查询请求
 */
@Data
@ApiModel(description = "模块查询请求")
public class ModuleQueryRequest {
    
    @ApiModelProperty(value = "模板版本ID")
    private Long templateVersionId;
    
    @ApiModelProperty(value = "模块名称")
    private String moduleName;
    
    @ApiModelProperty(value = "模块类型(1=功能权限模块 2=数据权限模块)")
    private Integer moduleType;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
} 