package com.ruantang.service.tenant.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.Tenant;
import com.ruantang.entity.tenant.TenantRelTenantTemplate;
import com.ruantang.service.tenant.model.dto.TenantDTO;

import java.util.List;

/**
 * 租户仓库接口
 */
public interface TenantRepository {
    
    /**
     * 分页查询租户
     * 
     * @param tenantName 租户名称（支持模糊查询）
     * @param tenantCode 租户编码
     * @param tenantType 租户类型
     * @param page 分页对象
     * @return 分页结果
     */
    Page<Tenant> queryTenantPage(String tenantName, String tenantCode, Integer tenantType, Page<Tenant> page);
    
    /**
     * 根据ID查询租户
     * 
     * @param id 租户ID
     * @return 租户信息
     */
    Tenant getTenantById(Long id);
    
    /**
     * 根据租户编码查询租户
     * 
     * @param tenantCode 租户编码
     * @return 租户信息
     */
    Tenant getTenantByCode(String tenantCode);
    
    /**
     * 创建租户
     * 
     * @param tenant 租户信息
     * @return 是否成功
     */
    boolean createTenant(Tenant tenant);
    
    /**
     * 更新租户
     * 
     * @param tenant 租户信息
     * @return 是否成功
     */
    boolean updateTenant(Tenant tenant);
    
    /**
     * 删除租户
     * 
     * @param id 租户ID
     * @return 是否成功
     */
    boolean deleteTenantById(Long id);
    
    /**
     * 为租户绑定模板
     * 
     * @param tenantRelTemplates 绑定关系列表
     * @return 是否成功
     */
    boolean bindTenantTemplates(List<TenantRelTenantTemplate> tenantRelTemplates);
    
    /**
     * 解绑租户与模板的关系
     * 
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean unbindTenantTemplate(Long tenantId, Long templateId);
    
    /**
     * 获取租户关联的所有模板
     * 
     * @param tenantId 租户ID
     * @return 模板关联列表
     */
    List<TenantRelTenantTemplate> getTenantTemplates(Long tenantId);
} 