package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据权限策略查询请求
 */
@Data
@ApiModel(description = "数据权限策略查询请求")
public class PolicyQueryRequest {
    
    @ApiModelProperty(value = "策略名称")
    private String policyName;
    
    @ApiModelProperty(value = "策略编码")
    private String policyCode;
    
    @ApiModelProperty(value = "条件类型(1:SQL片段 2:SpEL表达式)")
    private Integer conditionType;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
} 