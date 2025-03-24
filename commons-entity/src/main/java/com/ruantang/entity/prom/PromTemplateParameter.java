package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 提示词模板参数实体类
 */
@Data
@TableName("prom_template_parameter")
public class PromTemplateParameter {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关联模板表ID
     */
    private Long templateId;
    
    /**
     * 参数键
     */
    private String paramKey;
    
    /**
     * 标识模板类型（0-系统提示词或1-用户提示词）
     */
    private Integer promRole;
    
    /**
     * 参数默认值
     */
    private String defaultValue;
    
    /**
     * 输入提醒
     */
    private String enterReminder;
    
    /**
     * 变量描述
     */
    private String description;
    
    /**
     * 是否必填（1=是，0-否）
     */
    private Integer required;
    
//    /**
//     * 删除状态（0：删除，1：未删除）
//     */
//    @TableLogic
//    private Integer isDeleted;
    
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