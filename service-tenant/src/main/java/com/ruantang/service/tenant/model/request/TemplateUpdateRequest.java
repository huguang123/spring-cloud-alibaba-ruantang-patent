package com.ruantang.service.tenant.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 租户模板更新请求
 */
@Data
@ApiModel(value = "租户模板更新请求", description = "更新租户模板的参数")
public class TemplateUpdateRequest {

    @ApiModelProperty(value = "模板ID", required = true)
    @NotNull(message = "模板ID不能为空")
    private Long id;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板类型")
    private Integer templateType;

    @ApiModelProperty("行业类型")
    private Integer industryType;

    @ApiModelProperty("数据隔离模式")
    private Integer dataIsolationMode;

    @ApiModelProperty("是否系统内置模板")
    private Integer isSystem;

    @ApiModelProperty("模板描述")
    private String templateDesc;
} 