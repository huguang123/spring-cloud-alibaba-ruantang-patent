package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模板创建请求
 */
@Data
@ApiModel(description = "模板创建请求")
public class TemplateCreateRequest {
    
    @NotBlank(message = "模板编码不能为空")
    @ApiModelProperty(value = "模板编码", required = true)
    private String templateCode;
    
    @NotNull(message = "模板类型不能为空")
    @ApiModelProperty(value = "模板类型(1:平台配置模板 2:企业租户配置模板 3:代理所配置模板)", required = true)
    private Integer templateType;
    
    @ApiModelProperty(value = "基础描述")
    private String baseDescription;
} 