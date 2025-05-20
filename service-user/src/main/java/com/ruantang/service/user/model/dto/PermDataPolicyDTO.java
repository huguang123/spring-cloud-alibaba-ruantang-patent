package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据权限策略DTO
 */
@Data
@ApiModel(value = "数据权限策略DTO", description = "数据权限策略数据传输对象")
public class PermDataPolicyDTO {
    
    @ApiModelProperty(value = "主键ID")
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
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 