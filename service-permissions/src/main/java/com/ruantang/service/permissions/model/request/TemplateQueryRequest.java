package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板查询请求
 */
@Data
@ApiModel(description = "模板查询请求")
public class TemplateQueryRequest {
    
    @ApiModelProperty(value = "模板编码")
    private String templateCode;
    
    @ApiModelProperty(value = "模板类型(1:平台配置模板 2:企业租户配置模板 3:代理所配置模板)")
    private Integer templateType;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
} 