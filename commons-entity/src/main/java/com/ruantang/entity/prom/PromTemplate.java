package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 提示词模板实体类
 */
@Data
@TableName("prom_template")
public class PromTemplate {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 提示词模板类型
     */
    private String sectionType;
    
    /**
     * 提示词模板名称
     */
    private String promName;
    
    /**
     * 提示词模板内容
     */
    private String content;
    
    /**
     * 模板优先级
     */
    private Integer weight;
    
    /**
     * 模板版本
     */
    private String version;
    
    /**
     * 状态（0：启用，1：禁用）
     */
    private Boolean enabled;
    
    /**
     * 创建人
     */
    private Long createBy;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 