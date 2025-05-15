package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.ConfigPermModule;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.service.permissions.model.dto.ConfigPermModuleDTO;
import com.ruantang.service.permissions.model.request.ModuleCreateRequest;
import com.ruantang.service.permissions.model.request.ModuleQueryRequest;
import com.ruantang.service.permissions.model.request.ModuleUpdateRequest;
import com.ruantang.service.permissions.repository.ConfigPermModuleRepository;
import com.ruantang.service.permissions.repository.ConfigPermNodeRepository;
import com.ruantang.service.permissions.repository.ConfigPermVersionRepository;
import com.ruantang.service.permissions.service.ConfigPermModuleService;
import com.ruantang.service.permissions.util.ConfigPermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 配置权限模块服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConfigPermModuleServiceImpl implements ConfigPermModuleService {

    private final ConfigPermModuleRepository moduleRepository;
    private final ConfigPermVersionRepository versionRepository;
    private final ConfigPermNodeRepository nodeRepository;

    @Override
    public ApiResult<Page<ConfigPermModuleDTO>> queryModulePage(ModuleQueryRequest request) {
        Page<ConfigPermModule> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ConfigPermModule> modulePage = moduleRepository.queryModulePage(
                request.getTemplateVersionId(),
                request.getModuleName(),
                request.getModuleType(),
                page
        );
        
        // 转换为DTO
        Page<ConfigPermModuleDTO> resultPage = new Page<>(modulePage.getCurrent(), modulePage.getSize(), modulePage.getTotal());
        List<ConfigPermModuleDTO> records = ConfigPermUtil.convertToModuleDTOList(modulePage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<ConfigPermModuleDTO> getModuleById(Long id) {
        ConfigPermModule module = moduleRepository.getModuleById(id);
        if (module == null) {
            return ApiResult.failed("配置权限模块不存在");
        }
        
        return ApiResult.success(ConfigPermUtil.convertToModuleDTO(module));
    }

    @Override
    public ApiResult<List<ConfigPermModuleDTO>> listModulesByVersionId(Long templateVersionId) {
        // 检查版本是否存在
        ConfigPermVersion version = versionRepository.getVersionById(templateVersionId);
        if (version == null) {
            return ApiResult.failed("模板版本不存在");
        }
        
        List<ConfigPermModule> modules = moduleRepository.listModulesByVersionId(templateVersionId);
        List<ConfigPermModuleDTO> dtoList = ConfigPermUtil.convertToModuleDTOList(modules);
        
        return ApiResult.success(dtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<ConfigPermModuleDTO> createModule(ModuleCreateRequest request) {
        // 检查版本是否存在
        ConfigPermVersion version = versionRepository.getVersionById(request.getTemplateVersionId());
        if (version == null) {
            throw new ApiException("模板版本不存在");
        }
        
        ConfigPermModule module = new ConfigPermModule();
        BeanUtils.copyProperties(request, module);
        
        // 设置创建时间
        module.setCreateTime(System.currentTimeMillis());
        module.setUpdateTime(System.currentTimeMillis());
        
        // 生成ID (这里使用时间戳，实际应用中可以使用雪花算法等)
//        module.setId(System.currentTimeMillis());
        
        boolean success = moduleRepository.saveModule(module);
        if (!success) {
            return ApiResult.failed("创建配置权限模块失败");
        }
        
        return ApiResult.success(ConfigPermUtil.convertToModuleDTO(module));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateModule(ModuleUpdateRequest request) {
        // 检查模块是否存在
        ConfigPermModule existModule = moduleRepository.getModuleById(request.getId());
        if (existModule == null) {
            return ApiResult.failed("配置权限模块不存在");
        }
        
        ConfigPermModule module = new ConfigPermModule();
        BeanUtils.copyProperties(request, module);
        
        // 设置更新时间
        module.setUpdateTime(System.currentTimeMillis());
        
        boolean success = moduleRepository.updateModule(module);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteModuleById(Long id) {
        // 检查模块是否存在
        ConfigPermModule existModule = moduleRepository.getModuleById(id);
        if (existModule == null) {
            return ApiResult.failed("配置权限模块不存在");
        }
        
        // 删除模块下的所有节点
        nodeRepository.deleteNodesByModuleId(id);
        
        // 删除模块
        boolean success = moduleRepository.deleteModuleById(id);
        return ApiResult.success(success);
    }
} 