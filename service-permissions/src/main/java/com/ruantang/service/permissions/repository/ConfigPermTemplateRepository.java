package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.ConfigPermTemplate;
import com.ruantang.mapper.perm.ConfigPermTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * 配置权限模板数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class ConfigPermTemplateRepository {

    private final ConfigPermTemplateMapper templateMapper;
    
    /**
     * 分页查询配置权限模板
     */
    public Page<ConfigPermTemplate> queryTemplatePage(String templateCode, Integer templateType, Page<ConfigPermTemplate> page) {
        LambdaQueryWrapper<ConfigPermTemplate> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(templateCode)) {
            queryWrapper.like(ConfigPermTemplate::getTemplateCode, templateCode);
        }
        
        if (templateType != null) {
            queryWrapper.eq(ConfigPermTemplate::getTemplateType, templateType);
        }
        
        queryWrapper.orderByDesc(ConfigPermTemplate::getCreateTime);
        return templateMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询配置权限模板
     */
    public ConfigPermTemplate getTemplateById(Long id) {
        return templateMapper.selectById(id);
    }
    
    /**
     * 根据编码查询配置权限模板
     */
    public ConfigPermTemplate getTemplateByCode(String templateCode) {
        LambdaQueryWrapper<ConfigPermTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermTemplate::getTemplateCode, templateCode);
        return templateMapper.selectOne(queryWrapper);
    }
    
    /**
     * 根据模板类型查询配置权限模板
     */
    public ConfigPermTemplate getTemplateByType(Integer templateType) {
        LambdaQueryWrapper<ConfigPermTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermTemplate::getTemplateType, templateType);
        // 按创建时间降序，取第一条记录
        queryWrapper.orderByDesc(ConfigPermTemplate::getCreateTime);
        return templateMapper.selectOne(queryWrapper);
    }
    
    /**
     * 保存配置权限模板
     */
    public boolean saveTemplate(ConfigPermTemplate template) {
        return templateMapper.insert(template) > 0;
    }
    
    /**
     * 更新配置权限模板
     */
    public boolean updateTemplate(ConfigPermTemplate template) {
        return templateMapper.updateById(template) > 0;
    }
    
    /**
     * 删除配置权限模板
     */
    public boolean deleteTemplateById(Long id) {
        return templateMapper.deleteById(id) > 0;
    }
} 