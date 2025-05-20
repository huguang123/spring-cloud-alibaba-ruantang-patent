package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据权限策略更新请求
 */
@Data
@ApiModel(value = "数据权限策略更新请求", description = "数据权限策略更新请求参数")
public class PolicyUpdateRequest {
    
    @NotNull(message = "策略ID不能为空")
    @ApiModelProperty(value = "策略ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "策略编码")
    private String policyCode;
    
    @ApiModelProperty(value = "策略名称")
    private String policyName;
    
    @ApiModelProperty(value = "条件类型(1:SQL片段 2:SpEL表达式)")
    private Integer conditionType;
    
    @ApiModelProperty(value = "条件表达式")
    private String conditionExpression;
    
    @ApiModelProperty(value = "生效表(逗号分隔)")
    private String effectTables;
    
    @ApiModelProperty(value = "优先级")
    private Integer priority;
    
    @ApiModelProperty(value = "策略描述")
    private String policyDescription;
} 