package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户角色分配请求
 */
@Data
@ApiModel(value = "用户角色分配请求")
public class UserRoleAssignRequest {
    
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @ApiModelProperty(value = "角色ID列表", required = true)
    @NotNull(message = "角色ID列表不能为空")
    private List<Long> roleIds;
} 