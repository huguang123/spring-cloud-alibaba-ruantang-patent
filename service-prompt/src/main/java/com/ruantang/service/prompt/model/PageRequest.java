package com.ruantang.service.prompt.model;

import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageRequest {
    
    /**
     * 当前页码，从1开始
     */
    private Integer pageNum = 1;
    
    /**
     * 每页条数
     */
    private Integer pageSize = 10;
    
    /**
     * 获取起始行
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
} 