package com.ruantang.service.tenant.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 租户绑定模板请求
 */
@Data
public class TenantBindTemplatesRequest {
    
    /**
     * 租户ID，不能为空
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
    
    /**
     * 模板绑定列表，不能为空
     */
    @NotEmpty(message = "模板绑定列表不能为空")
    private List<TenantTemplateBindRequest> templates;
}