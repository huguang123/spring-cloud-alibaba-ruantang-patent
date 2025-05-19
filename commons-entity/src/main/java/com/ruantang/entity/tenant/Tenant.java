package com.ruantang.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 租户实体类
 */
@Data
@TableName("tenant")
public class Tenant {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 服务到期时间
     */
    private Long expireTime;
    
    /**
     * 租户管理员ID（关联sys_users.id）
     */
    private Long adminUserId;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 