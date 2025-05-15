package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.mapper.perm.ConfigPermVersionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 模板版本配置数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class ConfigPermVersionRepository {

    private final ConfigPermVersionMapper versionMapper;
    
    /**
     * 分页查询模板版本配置
     */
    public Page<ConfigPermVersion> queryVersionPage(Long templateId, Integer isDefault, Page<ConfigPermVersion> page) {
        LambdaQueryWrapper<ConfigPermVersion> queryWrapper = new LambdaQueryWrapper<>();
        
        if (templateId != null) {
            queryWrapper.eq(ConfigPermVersion::getTemplateId, templateId);
        }
        
        if (isDefault != null) {
            queryWrapper.eq(ConfigPermVersion::getIsDefault, isDefault);
        }
        
        queryWrapper.orderByDesc(ConfigPermVersion::getCreateTime);
        return versionMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询模板版本配置
     */
    public ConfigPermVersion getVersionById(Long id) {
        return versionMapper.selectById(id);
    }
    
    /**
     * 根据模板ID查询默认版本
     */
    public ConfigPermVersion getDefaultVersionByTemplateId(Long templateId) {
        LambdaQueryWrapper<ConfigPermVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermVersion::getTemplateId, templateId)
                   .eq(ConfigPermVersion::getIsDefault, 1);
        return versionMapper.selectOne(queryWrapper);
    }
    
    /**
     * 清除模板的默认版本状态（将所有默认版本设置为非默认）
     */
    public boolean clearDefaultVersion(Long templateId) {
        LambdaUpdateWrapper<ConfigPermVersion> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ConfigPermVersion::getTemplateId, templateId)
                    .eq(ConfigPermVersion::getIsDefault, 1)
                    .set(ConfigPermVersion::getIsDefault, 0);
        return versionMapper.update(null, updateWrapper) >= 0;
    }
    
    /**
     * 保存模板版本配置
     */
    public boolean saveVersion(ConfigPermVersion version) {
        return versionMapper.insert(version) > 0;
    }
    
    /**
     * 更新模板版本配置
     */
    public boolean updateVersion(ConfigPermVersion version) {
        return versionMapper.updateById(version) > 0;
    }
    
    /**
     * 删除模板版本配置
     */
    public boolean deleteVersionById(Long id) {
        return versionMapper.deleteById(id) > 0;
    }
} 