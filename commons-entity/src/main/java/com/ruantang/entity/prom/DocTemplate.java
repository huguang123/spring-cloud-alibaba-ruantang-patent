package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文档模板实体类
 */
@Data
@TableName("prom_doc_template")
public class DocTemplate {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关联文档模板分类表
     */
    private Long templateTypeId;
    
    /**
     * 关联技术领域
     */
    private Long domainId;
    
    /**
     * 关联组织表
     */
    private Long orgId;
    
    /**
     * 模板名称
     */
    private String templateName;
    
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