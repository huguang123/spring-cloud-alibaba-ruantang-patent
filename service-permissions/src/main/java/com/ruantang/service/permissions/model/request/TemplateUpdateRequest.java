package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模板更新请求
 */
@Data
@ApiModel(description = "模板更新请求")
public class TemplateUpdateRequest {
    
    @NotNull(message = "模板ID不能为空")
    @ApiModelProperty(value = "模板ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "模板编码")
    private String templateCode;
    
    @ApiModelProperty(value = "模板类型(1:平台配置模板 2:企业租户配置模板 3:代理所配置模板)")
    private Integer templateType;
    
    @ApiModelProperty(value = "基础描述")
    private String baseDescription;
}
