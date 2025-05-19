package com.ruantang.service.tenant.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 租户创建请求
 */
@Data
public class TenantCreateRequest {
    
    /**
     * 租户编码，不能为空
     */
    @NotBlank(message = "租户编码不能为空")
    private String tenantCode;
    
    /**
     * 租户名称，不能为空
     */
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;
    
    /**
     * 租户类型（1：平台管理租户、2：企业商户租户、3：代理所租户）
     */
    @NotNull(message = "租户类型不能为空")
    private Integer tenantType;
    
    /**
     * 联系电话
     */
    private String tenantPhone;
    
    /**
     * 联系邮箱
     */
    private String tenantEmail;
    
    /**
     * 备注说明
     */
    private String description;
    
    /**
     * 租户地址
     */
    private String tenantAddress;
    
    /**
     * 数据隔离模式（1=行级 2=Schema 3=独立库）
     */
    @NotNull(message = "数据隔离模式不能为空")
    private Integer dataIsolationMode;
    
    /**
     * 服务到期时间
     */
    private Long expireTime;
    
    /**
     * 租户管理员ID
     */
    private Long adminUserId;
    
    /**
     * 创建时关联的模板
     */
    private List<TenantTemplateBindRequest> templates;
}