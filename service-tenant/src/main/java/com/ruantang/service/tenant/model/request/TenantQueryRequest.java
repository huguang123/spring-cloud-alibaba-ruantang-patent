package com.ruantang.service.tenant.model.request;

import lombok.Data;

/**
 * 租户查询请求
 */
@Data
public class TenantQueryRequest {
    
    /**
     * 租户名称（支持模糊查询）
     */
    private String tenantName;
    
    /**
     * 租户编码
     */
    private String tenantCode;
    
    /**
     * 租户类型
     */
    private Integer tenantType;
    
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页条数
     */
    private Integer pageSize = 10;
} 