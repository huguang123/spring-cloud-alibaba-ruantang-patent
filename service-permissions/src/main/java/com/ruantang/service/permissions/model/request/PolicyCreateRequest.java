package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据权限策略创建请求
 */
@Data
@ApiModel(description = "数据权限策略创建请求")
public class PolicyCreateRequest {
    
    @NotBlank(message = "策略名称不能为空")
    @ApiModelProperty(value = "策略名称", required = true)
    private String policyName;
    
    @NotBlank(message = "策略编码不能为空")
    @ApiModelProperty(value = "策略编码", required = true)
    private String policyCode;
    
    @NotNull(message = "条件类型不能为空")
    @ApiModelProperty(value = "条件类型(1:SQL片段 2:SpEL表达式)", required = true)
    private Integer conditionType;
    
    @NotBlank(message = "条件表达式不能为空")
    @ApiModelProperty(value = "条件表达式", required = true)
    private String conditionExpression;
    
    @NotBlank(message = "生效表不能为空")
    @ApiModelProperty(value = "生效表(逗号分隔)", required = true)
    private String effectTables;
    
    @ApiModelProperty(value = "优先级")
    private Integer priority = 50;

    @ApiModelProperty(value = "策略描述")
    private String policyDescription;
} 