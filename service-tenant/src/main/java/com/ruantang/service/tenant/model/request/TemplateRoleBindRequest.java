package com.ruantang.service.tenant.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 模板角色绑定请求
 */
@Data
@ApiModel(value = "模板角色绑定请求", description = "为模板绑定角色的参数")
public class TemplateRoleBindRequest {

    @ApiModelProperty(value = "模板ID", required = true)
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @ApiModelProperty("角色绑定列表")
    private List<RoleBindItem> roleBinds;

    /**
     * 角色绑定项
     */
    @Data
    public static class RoleBindItem {
        @ApiModelProperty(value = "角色ID", required = true)
        @NotNull(message = "角色ID不能为空")
        private Long roleId;

        @ApiModelProperty(value = "是否继承权限变更", required = true)
        @NotNull(message = "是否继承权限变更不能为空")
        private Integer isInherit;
    }
} 