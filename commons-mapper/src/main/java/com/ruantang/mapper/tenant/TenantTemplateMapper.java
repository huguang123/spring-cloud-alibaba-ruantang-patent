package com.ruantang.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.TenantTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 租户模板数据访问接口
 */
@Repository
@Mapper
public interface TenantTemplateMapper extends BaseMapper<TenantTemplate> {
    
    /**
     * 分页查询租户模板
     * 
     * @param templateName 模板名称（支持模糊查询）
     * @param templateCode 模板编码
     * @param templateType 模板类型
     * @param industryType 行业类型
     * @param page 分页对象
     * @return 分页结果
     */
    Page<TenantTemplate> queryTemplatePage(
            @Param("templateName") String templateName,
            @Param("templateCode") String templateCode,
            @Param("templateType") Integer templateType,
            @Param("industryType") Integer industryType,
            @Param("page") Page<TenantTemplate> page
    );
} 