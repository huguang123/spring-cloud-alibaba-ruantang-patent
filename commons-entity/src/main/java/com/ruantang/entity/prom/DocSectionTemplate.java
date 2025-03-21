package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文档分项模板实体类
 */
@Data
@TableName("prom_doc_section_template")
public class DocSectionTemplate {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关联文档模板
     */
    private Long docTemplateId;
    
    /**
     * 关联提示词模板
     */
    private Long promptId;
    
    /**
     * 分项名称
     */
    private String sectionName;
    
    /**
     * 显示顺序
     */
    private Integer sortOrder;
    
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