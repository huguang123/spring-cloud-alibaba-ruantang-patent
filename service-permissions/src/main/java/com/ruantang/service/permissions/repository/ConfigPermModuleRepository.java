package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.ConfigPermModule;
import com.ruantang.mapper.perm.ConfigPermModuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 配置权限模块数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class ConfigPermModuleRepository {

    private final ConfigPermModuleMapper moduleMapper;
    
    /**
     * 分页查询配置权限模块
     */
    public Page<ConfigPermModule> queryModulePage(Long templateVersionId, String moduleName, Integer moduleType, Page<ConfigPermModule> page) {
        LambdaQueryWrapper<ConfigPermModule> queryWrapper = new LambdaQueryWrapper<>();
        
        if (templateVersionId != null) {
            queryWrapper.eq(ConfigPermModule::getTemplateVersionId, templateVersionId);
        }
        
        if (StringUtils.hasText(moduleName)) {
            queryWrapper.like(ConfigPermModule::getModuleName, moduleName);
        }
        
        if (moduleType != null) {
            queryWrapper.eq(ConfigPermModule::getModuleType, moduleType);
        }
        
        queryWrapper.orderByAsc(ConfigPermModule::getSort);
        return moduleMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询配置权限模块
     */
    public ConfigPermModule getModuleById(Long id) {
        return moduleMapper.selectById(id);
    }
    
    /**
     * 根据模板版本ID查询模块列表
     */
    public List<ConfigPermModule> listModulesByVersionId(Long templateVersionId) {
        LambdaQueryWrapper<ConfigPermModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermModule::getTemplateVersionId, templateVersionId)
                   .orderByAsc(ConfigPermModule::getSort);
        return moduleMapper.selectList(queryWrapper);
    }
    
    /**
     * 保存配置权限模块
     */
    public boolean saveModule(ConfigPermModule module) {
        return moduleMapper.insert(module) > 0;
    }
    
    /**
     * 更新配置权限模块
     */
    public boolean updateModule(ConfigPermModule module) {
        return moduleMapper.updateById(module) > 0;
    }
    
    /**
     * 删除配置权限模块
     */
    public boolean deleteModuleById(Long id) {
        return moduleMapper.deleteById(id) > 0;
    }
    
    /**
     * 根据模板版本ID删除所有模块
     */
    public boolean deleteModulesByVersionId(Long templateVersionId) {
        LambdaQueryWrapper<ConfigPermModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermModule::getTemplateVersionId, templateVersionId);
        return moduleMapper.delete(queryWrapper) >= 0;
    }
}
