package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 租户角色验证请求
 */
@Data
@ApiModel(description = "租户角色验证请求")
public class TenantRoleVerifyRequest {
    
    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    
    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色ID列表不能为空")
    @ApiModelProperty(value = "角色ID列表", required = true)
    private List<Long> roleIds;
} 