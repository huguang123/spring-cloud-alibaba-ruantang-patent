package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 配置权限模板实体类
 */
@Data
@TableName("config_perm_template")
public class ConfigPermTemplate {
    
    /**
     * 模板ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 模板编码（唯一标识）
     */
    private String templateCode;
    
    /**
     * 模板类型（1:平台配置模板 2:企业租户配置模板 3:代理所配置模板）
     */
    private Integer templateType;
    
    /**
     * 基础描述
     */
    private String baseDescription;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 