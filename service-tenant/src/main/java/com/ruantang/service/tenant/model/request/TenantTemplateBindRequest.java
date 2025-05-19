package com.ruantang.service.tenant.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 租户模板绑定请求
 */
@Data
public class TenantTemplateBindRequest {
    
    /**
     * 模板ID
     */
    @NotNull(message = "模板ID不能为空")
    private Long templateId;
    
    /**
     * 绑定模式(1:继承 2:快照)
     */
    @NotNull(message = "绑定模式不能为空")
    private Integer bindMode;
    
    /**
     * 生效时间戳
     */
    private Long effectiveTime;
}