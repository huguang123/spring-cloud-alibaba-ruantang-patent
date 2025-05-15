package com.ruantang.entity.perm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据权限策略实体类
 */
@Data
@TableName("perm_data_policy")
public class PermDataPolicy {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 策略编码
     */
    private String policyCode;
    
    /**
     * 策略名称
     */
    private String policyName;
    
    /**
     * 条件类型(1:SQL片段 2:SpEL表达式)
     */
    private Integer conditionType;
    
    /**
     * 条件表达式
     */
    private String conditionExpression;
    
    /**
     * 生效表(逗号分隔)
     */
    private String effectTables;
    
    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 策略描述
     */
    private  String policyDescription;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 