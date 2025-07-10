package com.ruantang.service.tenant.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 租户用户角色权限查询请求
 */
@Data
@ApiModel(description = "租户用户角色权限查询请求")
public class TenantUserRolesRequest {
    
    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
} 