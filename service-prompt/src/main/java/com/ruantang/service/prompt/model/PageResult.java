package com.ruantang.service.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * 分页结果DTO
 */
@Data
public class PageResult<T> {
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页数据
     */
    private List<T> records;
    
    /**
     * 当前页码
     */
    private Integer pageNum;
    
    /**
     * 每页条数
     */
    private Integer pageSize;
    
    /**
     * 总页数
     */
    private Integer pages;
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Integer pageNum, Integer pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        // 计算总页数
        if (total == 0) {
            result.setPages(0);
        } else {
            result.setPages((int) ((total + pageSize - 1) / pageSize));
        }
        
        return result;
    }
} 