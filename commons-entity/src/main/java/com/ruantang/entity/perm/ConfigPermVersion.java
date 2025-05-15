package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 模板版本配置实体类
 */
@Data
@TableName("config_perm_version")
public class ConfigPermVersion {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关联模板ID
     */
    private Long templateId;
    
    /**
     * 语义化版本号
     */
    private String version;
    
    /**
     * 是否默认版本
     */
    private Integer isDefault;
    
    /**
     * 版本描述
     */
    private String versionDescription;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 