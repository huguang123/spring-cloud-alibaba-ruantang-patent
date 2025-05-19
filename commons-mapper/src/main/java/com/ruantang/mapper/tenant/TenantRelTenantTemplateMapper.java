package com.ruantang.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.tenant.TenantRelTenantTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户模板关联数据访问接口
 */
@Repository
public interface TenantRelTenantTemplateMapper extends BaseMapper<TenantRelTenantTemplate> {

    /**
     * 根据租户ID查询所有关联的模板
     *
     * @param tenantId 租户ID
     * @return 模板关联列表
     */
    List<TenantRelTenantTemplate> listTemplatesByTenantId(@Param("tenantId") Long tenantId);
    
    /**
     * 根据租户ID删除所有模板关联
     *
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int deleteByTenantId(@Param("tenantId") Long tenantId);
    
    /**
     * 根据租户ID和模板ID删除关联
     *
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @return 影响行数
     */
    int deleteByTenantIdAndTemplateId(@Param("tenantId") Long tenantId, @Param("templateId") Long templateId);
} 