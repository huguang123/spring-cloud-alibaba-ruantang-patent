package com.ruantang.service.tenant.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 租户模板创建请求
 */
@Data
@ApiModel(value = "租户模板创建请求", description = "创建租户模板的参数")
public class TemplateCreateRequest {

    @ApiModelProperty(value = "模板编码", required = true)
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    @ApiModelProperty(value = "模板名称", required = true)
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    @ApiModelProperty(value = "模板类型", required = true)
    @NotNull(message = "模板类型不能为空")
    private Integer templateType;

    @ApiModelProperty(value = "行业类型", required = true)
    @NotNull(message = "行业类型不能为空")
    private Integer industryType;

    @ApiModelProperty("数据隔离模式")
    private Integer dataIsolationMode;

    @ApiModelProperty("是否系统内置模板")
    private Integer isSystem = 0;

    @ApiModelProperty("模板描述")
    private String templateDesc;
} 