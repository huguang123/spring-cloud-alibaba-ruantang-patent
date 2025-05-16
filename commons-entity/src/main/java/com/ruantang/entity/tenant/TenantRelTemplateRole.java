package com.ruantang.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 租户模板角色关联实体类
 */
@Data
@TableName("tenant_rel_template_role")
public class TenantRelTemplateRole {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 是否继承权限变更
     */
    private Integer isInherit;
    
    /**
     * 权限快照(创建时JSON结构)
     * 如果继承权限变更，这里为空，如果不继承权限变更，需要存储权限快照
     */
    private String permissionSnapshot;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 