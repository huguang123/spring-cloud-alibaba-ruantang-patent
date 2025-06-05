package com.ruantang.service.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * Word文档导出请求DTO
 */
@Data
public class DocWordExportRequest {
    
    /**
     * 文档标题
     */
    private String title;
    
    /**
     * 文档分项列表
     */
    private List<DocSection> sections;
    
    /**
     * 导出文件名（可选，不传则自动生成）
     */
    private String fileName;
    
    @Data
    public static class DocSection {
        /**
         * 分项名称
         */
        private String sectionName;
        
        /**
         * 分项内容
         */
        private String content;
        
        /**
         * 排序顺序
         */
        private Integer sortOrder;
    }
} 