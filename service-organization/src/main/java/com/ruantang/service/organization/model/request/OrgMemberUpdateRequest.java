package com.ruantang.service.organization.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 组织成员更新请求
 */
@Data
@ApiModel(description = "组织成员更新请求")
public class OrgMemberUpdateRequest {
    
    @NotNull(message = "组织ID不能为空")
    @ApiModelProperty(value = "组织ID", required = true)
    private Long orgId;
    
    @NotNull(message = "租户ID不能为空")
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
    
    @ApiModelProperty(value = "用户名称")
    private String userName;
    
    @ApiModelProperty(value = "用户手机号")
    private String userPhone;
    
    @ApiModelProperty(value = "用户邮箱")
    private String userMail;
    
    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIds;
} 