package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色操作权限关联实体类
 */
@Data
@TableName("rel_roles_perm")
public class RelRolesPerm {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 角色ID
     */
    private Long rolesId;
    
    /**
     * 权限ID
     */
    private Long permsId;
    
    /**
     * 创建时间
     */
    private Long createTime;
} 