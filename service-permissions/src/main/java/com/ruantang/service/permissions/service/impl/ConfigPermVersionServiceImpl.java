package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.ConfigPermTemplate;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.service.permissions.model.dto.ConfigPermVersionDTO;
import com.ruantang.service.permissions.model.request.VersionCreateRequest;
import com.ruantang.service.permissions.model.request.VersionQueryRequest;
import com.ruantang.service.permissions.model.request.VersionUpdateRequest;
import com.ruantang.service.permissions.repository.ConfigPermTemplateRepository;
import com.ruantang.service.permissions.repository.ConfigPermVersionRepository;
import com.ruantang.service.permissions.service.ConfigPermVersionService;
import com.ruantang.service.permissions.util.ConfigPermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模板版本配置服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConfigPermVersionServiceImpl implements ConfigPermVersionService {

    private final ConfigPermVersionRepository versionRepository;
    private final ConfigPermTemplateRepository templateRepository;

    @Override
    public ApiResult<Page<ConfigPermVersionDTO>> queryVersionPage(VersionQueryRequest request) {
        Page<ConfigPermVersion> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ConfigPermVersion> versionPage = versionRepository.queryVersionPage(
                request.getTemplateId(),
                request.getIsDefault(),
                page
        );
        
        // 转换为DTO
        Page<ConfigPermVersionDTO> resultPage = new Page<>(versionPage.getCurrent(), versionPage.getSize(), versionPage.getTotal());
        List<ConfigPermVersionDTO> records = ConfigPermUtil.convertToVersionDTOList(versionPage.getRecords());
        
        // 设置模板名称
        if (!records.isEmpty()) {
            for (ConfigPermVersionDTO dto : records) {
                if (dto.getTemplateId() != null) {
                    ConfigPermTemplate template = templateRepository.getTemplateById(dto.getTemplateId());
                    if (template != null) {
                        dto.setTemplateName(template.getTemplateCode());
                    }
                }
            }
        }
        
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<ConfigPermVersionDTO> getVersionById(Long id) {
        ConfigPermVersion version = versionRepository.getVersionById(id);
        if (version == null) {
            return ApiResult.failed("版本配置不存在");
        }
        
        ConfigPermVersionDTO dto = ConfigPermUtil.convertToVersionDTO(version);
        
        // 设置模板名称
        if (dto.getTemplateId() != null) {
            ConfigPermTemplate template = templateRepository.getTemplateById(dto.getTemplateId());
            if (template != null) {
                dto.setTemplateName(template.getTemplateCode());
            }
        }
        
        return ApiResult.success(dto);
    }

    @Override
    public ApiResult<ConfigPermVersionDTO> getDefaultVersionByTemplateId(Long templateId) {
        // 检查模板是否存在
        ConfigPermTemplate template = templateRepository.getTemplateById(templateId);
        if (template == null) {
            return ApiResult.failed("配置权限模板不存在");
        }
        
        ConfigPermVersion version = versionRepository.getDefaultVersionByTemplateId(templateId);
        if (version == null) {
            return ApiResult.failed("该模板没有默认版本");
        }
        
        ConfigPermVersionDTO dto = ConfigPermUtil.convertToVersionDTO(version);
        dto.setTemplateName(template.getTemplateCode());
        
        return ApiResult.success(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<ConfigPermVersionDTO> createVersion(VersionCreateRequest request) {
        // 检查模板是否存在
        ConfigPermTemplate template = templateRepository.getTemplateById(request.getTemplateId());
        if (template == null) {
            return ApiResult.failed("配置权限模板不存在");
        }
        
        ConfigPermVersion version = new ConfigPermVersion();
        BeanUtils.copyProperties(request, version);
        
        // 设置创建时间
        version.setCreateTime(System.currentTimeMillis());
        version.setUpdateTime(System.currentTimeMillis());
        
        // 生成ID (这里使用时间戳，实际应用中可以使用雪花算法等)
//        version.setId(System.currentTimeMillis());
        
        // 处理默认版本
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            // 清除该模板下其它默认版本
            versionRepository.clearDefaultVersion(request.getTemplateId());
        }
        
        boolean success = versionRepository.saveVersion(version);
        if (!success) {
            return ApiResult.failed("创建版本配置失败");
        }
        
        ConfigPermVersionDTO dto = ConfigPermUtil.convertToVersionDTO(version);
        dto.setTemplateName(template.getTemplateCode());
        
        return ApiResult.success(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateVersion(VersionUpdateRequest request) {
        // 检查版本是否存在
        ConfigPermVersion existVersion = versionRepository.getVersionById(request.getId());
        if (existVersion == null) {
            return ApiResult.failed("版本配置不存在");
        }
        
        ConfigPermVersion version = new ConfigPermVersion();
        BeanUtils.copyProperties(request, version);
        
        // 设置更新时间
        version.setUpdateTime(System.currentTimeMillis());
        
        // 处理默认版本
        if (request.getIsDefault() != null && request.getIsDefault() == 1 && 
            (existVersion.getIsDefault() == null || existVersion.getIsDefault() != 1)) {
            // 清除该模板下其它默认版本
            versionRepository.clearDefaultVersion(existVersion.getTemplateId());
        }
        
        boolean success = versionRepository.updateVersion(version);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteVersionById(Long id) {
        // 检查版本是否存在
        ConfigPermVersion existVersion = versionRepository.getVersionById(id);
        if (existVersion == null) {
            return ApiResult.failed("版本配置不存在");
        }
        
        // 检查是否为默认版本，默认版本不允许删除
        if (existVersion.getIsDefault() != null && existVersion.getIsDefault() == 1) {
            return ApiResult.failed("默认版本不允许删除");
        }
        
        boolean success = versionRepository.deleteVersionById(id);
        return ApiResult.success(success);
    }
} 