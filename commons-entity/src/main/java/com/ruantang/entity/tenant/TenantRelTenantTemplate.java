package com.ruantang.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 租户模板关联实体类
 */
@Data
@TableName("tenant_rel_tenant_template")
public class TenantRelTenantTemplate {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 绑定模式(1:继承 2:快照) 企业模板绑定暂时只支持继承
     */
    private Integer bindMode;
    
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