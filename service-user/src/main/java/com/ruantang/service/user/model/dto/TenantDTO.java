package com.ruantang.service.user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 租户DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantDTO {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 租户编码
     */
    private String tenantCode;
    
    /**
     * 租户名称
     */
    private String tenantName;
    
    /**
     * 租户类型（1：平台管理租户、2：企业商户租户、3：代理所租户）
     */
    private Integer tenantType;
    
    /**
     * 租户类型名称
     */
    private String tenantTypeName;
    
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
    private Integer dataIsolationMode;
    
    /**
     * 数据隔离模式名称
     */
    private String dataIsolationModeName;
    
    /**
     * 服务到期时间
     */
    private Long expireTime;
    
    /**
     * 租户管理员ID
     */
    private Long adminUserId;
    
    /**
     * 租户管理员账号
     */
    private String adminUsername;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
    
//    /**
//     * 关联的模板列表
//     */
//    private List<TenantTemplateBindDTO> templates;
} 