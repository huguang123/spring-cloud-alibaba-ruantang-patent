package com.ruantang.entity.rel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户-组织关联实体
 */
@Data
@EqualsAndHashCode
@TableName("rel_user_organization")
public class RelUserOrganization {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 组织ID
     */
    @TableField("org_id")
    private Long orgId;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;
} 