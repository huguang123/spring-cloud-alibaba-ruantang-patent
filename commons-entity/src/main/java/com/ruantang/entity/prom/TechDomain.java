package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 技术领域实体类
 */
@Data
@TableName("prom_tech_domain")
public class TechDomain {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 领域名称
     */
    private String domainName;
    
    /**
     * 父级领域ID
     */
    private Long parentId;
    
    /**
     * 层级深度
     */
    private Integer level;
    
    /**
     * 领域描述
     */
    private String description;
    
    /**
     * 删除状态（0：删除，1：未删除）
     */
    @TableLogic
    private Integer isDeleted;
    
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