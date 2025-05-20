package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据权限策略查询请求
 */
@Data
@ApiModel(value = "数据权限策略查询请求", description = "数据权限策略查询请求参数")
public class PolicyQueryRequest {
    
    @ApiModelProperty(value = "策略编码(支持模糊查询)")
    private String policyCode;
    
    @ApiModelProperty(value = "策略名称(支持模糊查询)")
    private String policyName;
    
    @ApiModelProperty(value = "条件类型(1:SQL片段 2:SpEL表达式)")
    private Integer conditionType;
    
    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;
} 