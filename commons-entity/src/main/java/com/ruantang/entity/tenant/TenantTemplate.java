package com.ruantang.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 租户模板实体类
 */
@Data
@TableName("tenant_template")
public class TenantTemplate {
    
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 模板编码(如: EDU_TEMPLATE)
     */
    private String templateCode;
    
    /**
     * 模板名称(如: 教育行业模板)
     */
    private String templateName;
    
    /**
     * 模板类型（1：平台租户模板、2：企业租户模板、3：代理所租户模板）
     */
    private Integer templateType;
    
    /**
     * 行业类型(0:教育 1:医疗 2:金融)
     */
    private Integer industryType;
    
    /**
     * 数据隔离模式(继承租户配置)
     */
    private Integer dataIsolationMode;
    
    /**
     * 是否系统内置模板
     */
    private Integer isSystem;

    /**
     * 模板描述
     */
    private String templateDesc;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 