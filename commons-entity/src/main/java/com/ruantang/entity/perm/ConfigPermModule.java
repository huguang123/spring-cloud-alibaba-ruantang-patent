package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 配置权限模块实体类
 */
@Data
@TableName("config_perm_module")
public class ConfigPermModule {
    
    /**
     * 权限配置模块ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 模块名称（如：数据看板、交底书管理）
     */
    private String moduleName;
    
    /**
     * 模板版本ID
     */
    private Long templateVersionId;
    
    /**
     * 模块类型（1=功能权限模块 2=数据权限模块）
     */
    private Integer moduleType;
    
    /**
     * 排序号
     */
    private Integer sort;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 