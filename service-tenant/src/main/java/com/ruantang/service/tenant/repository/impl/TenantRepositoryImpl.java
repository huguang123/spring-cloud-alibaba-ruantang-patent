package com.ruantang.service.tenant.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.Tenant;
import com.ruantang.entity.tenant.TenantRelTenantTemplate;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.mapper.tenant.TenantMapper;
import com.ruantang.mapper.tenant.TenantRelTenantTemplateMapper;
import com.ruantang.service.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {
    
    private final TenantMapper tenantMapper;
    private final TenantRelTenantTemplateMapper tenantRelTenantTemplateMapper;

    @Override
    public Page<Tenant> queryTenantPage(String tenantName, String tenantCode, Integer tenantType, Page<Tenant> page) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件
        if (StringUtils.hasText(tenantName)) {
            queryWrapper.like(Tenant::getTenantName, tenantName);
        }

        if (StringUtils.hasText(tenantCode)) {
            queryWrapper.eq(Tenant::getTenantCode, tenantCode);
        }

        if (tenantType != null) {
            queryWrapper.eq(Tenant::getTenantType, tenantType);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(Tenant::getCreateTime);

        return tenantMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Tenant getTenantById(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public Tenant getTenantByCode(String tenantCode) {
        if (!StringUtils.hasText(tenantCode)) {
            return null;
        }
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getTenantCode, tenantCode);
        return tenantMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean createTenant(Tenant tenant) {
        return tenantMapper.insert(tenant) > 0;
    }

    @Override
    public boolean updateTenant(Tenant tenant) {
        return tenantMapper.updateById(tenant) > 0;
    }

    @Override
    public boolean deleteTenantById(Long id) {
        return tenantMapper.deleteById(id) > 0;
    }

    @Override
    public boolean bindTenantTemplates(List<TenantRelTenantTemplate> tenantRelTemplates) {
        if (tenantRelTemplates == null || tenantRelTemplates.isEmpty()) {
            return false;
        }
        // 批量插入
        for (TenantRelTenantTemplate template : tenantRelTemplates) {
            tenantRelTenantTemplateMapper.insert(template);
        }
        return true;
    }

    @Override
    public boolean unbindTenantTemplate(Long tenantId, Long templateId) {
        LambdaQueryWrapper<TenantRelTenantTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTenantTemplate::getTemplateId, templateId);
        queryWrapper.eq(TenantRelTenantTemplate::getTenantId,tenantId);
        return tenantRelTenantTemplateMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<TenantRelTenantTemplate> getTenantTemplates(Long tenantId) {
        LambdaQueryWrapper<TenantRelTenantTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTenantTemplate::getTenantId,tenantId);
        return tenantRelTenantTemplateMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isTemplateBoundToTenant(Long templateId) {
        if (templateId == null) {
            return false;
        }
        LambdaQueryWrapper<TenantRelTenantTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTenantTemplate::getTemplateId, templateId);
        return tenantRelTenantTemplateMapper.selectCount(queryWrapper) > 0;
    }
} 