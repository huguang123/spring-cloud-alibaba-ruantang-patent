package com.ruantang.service.organization.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组织成员查询请求
 */
@Data
@ApiModel(description = "组织成员查询请求")
public class OrgMemberQueryRequest {
    
    @NotNull(message = "组织ID不能为空")
    @ApiModelProperty(value = "组织ID", required = true)
    private Long orgId;
    
    @NotNull(message = "租户ID不能为空")
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    
    @ApiModelProperty(value = "用户名称")
    private String userName;
    
    @ApiModelProperty(value = "账号")
    private String loginName;
    
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页大小", required = true)
    private Integer pageSize = 10;
} 