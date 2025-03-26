package com.ruantang.service.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * 文档生成响应DTO
 */
@Data
public class DocGenerateResponse {
    
    /**
     * 文档标题
     */
    private String title;
    
    /**
     * 文档分项列表
     */
    private List<DocSection> sections;
    
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