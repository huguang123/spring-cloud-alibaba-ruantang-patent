package com.ruantang.service.tenant.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.mapper.tenant.TenantTemplateMapper;
import com.ruantang.service.tenant.repository.TenantTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * 租户模板数据访问仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class TenantTemplateRepositoryImpl implements TenantTemplateRepository {

    private final TenantTemplateMapper templateMapper;

    @Override
    public Page<TenantTemplate> queryTemplatePage(String templateName, String templateCode, 
                                                Integer templateType, Integer industryType, 
                                                Integer isSystem, Page<TenantTemplate> page) {
        LambdaQueryWrapper<TenantTemplate> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        if (StringUtils.hasText(templateName)) {
            queryWrapper.like(TenantTemplate::getTemplateName, templateName);
        }
        
        if (StringUtils.hasText(templateCode)) {
            queryWrapper.eq(TenantTemplate::getTemplateCode, templateCode);
        }
        
        if (templateType != null) {
            queryWrapper.eq(TenantTemplate::getTemplateType, templateType);
        }
        
        if (industryType != null) {
            queryWrapper.eq(TenantTemplate::getIndustryType, industryType);
        }
        
        if (isSystem != null) {
            queryWrapper.eq(TenantTemplate::getIsSystem, isSystem);
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(TenantTemplate::getCreateTime);
        
        return templateMapper.selectPage(page, queryWrapper);
    }

    @Override
    public TenantTemplate getTemplateById(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public TenantTemplate getTemplateByCode(String templateCode) {
        if (!StringUtils.hasText(templateCode)) {
            return null;
        }
        
        LambdaQueryWrapper<TenantTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantTemplate::getTemplateCode, templateCode);
        
        return templateMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean saveTemplate(TenantTemplate template) {
        return templateMapper.insert(template) > 0;
    }

    @Override
    public boolean updateTemplate(TenantTemplate template) {
        return templateMapper.updateById(template) > 0;
    }

    @Override
    public boolean deleteTemplateById(Long id) {
        return templateMapper.deleteById(id) > 0;
    }
} 