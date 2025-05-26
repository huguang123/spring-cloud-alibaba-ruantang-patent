package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 操作权限实体类
 */
@Data
@TableName("perm")
public class Perm {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 权限标识(如order:view)
     */
    private String permsCode;

    /**
     * 权限类型（API BUTTON）
     */
    private String permType;
    
    /**
     * 权限名称
     */
    private String permsName;
    
    /**
     * HTTP方法(GET、POST、PUT、DELETE)
     */
    private String apiMethod;
    
    /**
     * 接口路径
     */
    private String apiPath;
    
    /**
     * 权限作用域（PLATFORM:平台角色/TENANT:租户角色/ALL:通用）
     */
    private String permScope;
    
    /**
     * 权限描述
     */
    private String permsDescription;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 