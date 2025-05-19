package com.ruantang.service.tenant.model.dto;

import lombok.Data;

/**
 * 租户绑定的模板DTO
 */
@Data
public class TenantTemplateBindDTO {
    
    /**
     * 关联ID
     */
    private Long id;
    
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板编码
     */
    private String templateCode;
    
    /**
     * 绑定模式(1:继承 2:快照)
     */
    private Integer bindMode;
    
    /**
     * 绑定模式名称
     */
    private String bindModeName;
    
    /**
     * 生效时间戳
     */
    private Long effectiveTime;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 