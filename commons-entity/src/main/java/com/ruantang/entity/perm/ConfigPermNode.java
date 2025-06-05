package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 配置权限节点实体类
 */
@Data
@TableName("config_perm_node")
public class ConfigPermNode {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 显示名称（如：查看数据看板）
     */
    private String nodeName;
    
    /**
     * 所属模块ID
     */
    private Long moduleId;
    
    /**
     * 节点类型（1=菜单项 2=操作按钮 3=数据字段）
     */
    private Integer nodeType;
    
    /**
     * 绑定权限类型（0:操作权限 1:数据权限）
     */
    private Integer permType;
    
    /**
     * 绑定操作权限ID
     */
    private Long permId;
    
    /**
     * 绑定数据权限ID
     */
    private Long dataPolicyId;
    
    /**
     * 是否基础权限（0=自定义 1=系统预置）
     */
    private Integer isBasic;

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