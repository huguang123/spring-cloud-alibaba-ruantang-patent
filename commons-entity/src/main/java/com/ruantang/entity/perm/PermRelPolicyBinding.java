package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 策略绑定关系实体类
 */
@Data
@TableName("perm_rel_policy_binding")
public class PermRelPolicyBinding {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 策略ID
     */
    private Long policyId;
    
    /**
     * 绑定类型(1:角色 2:租户)
     */
    private Integer bindType;
    
    /**
     * 角色ID/租户ID
     */
    private Long bindId;
    
    /**
     * 创建时间
     */
    private Long createTime;
} 