package com.ruantang.entity.prom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文档模板分类实体类
 */
@Data
@TableName("prom_doc_template_type")
public class DocTemplateType {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 模板类型说明
     */
    private String typeName;
    
    /**
     * 模板类型描述
     */
    private String description;
    
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