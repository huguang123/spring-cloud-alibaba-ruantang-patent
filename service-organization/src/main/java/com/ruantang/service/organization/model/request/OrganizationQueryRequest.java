package com.ruantang.service.organization.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织查询请求
 */
@Data
@ApiModel(description = "组织查询请求")
public class OrganizationQueryRequest {
    
    @ApiModelProperty(value = "组织名称")
    private String orgName;
    
    @ApiModelProperty(value = "组织编码")
    private String orgCode;
    
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    
    @ApiModelProperty(value = "状态（0：禁用、1：启用）")
    private Integer state;
    
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页大小", required = true)
    private Integer pageSize = 10;
} 