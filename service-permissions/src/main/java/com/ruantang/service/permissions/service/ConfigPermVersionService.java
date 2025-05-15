package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermVersionDTO;
import com.ruantang.service.permissions.model.request.VersionCreateRequest;
import com.ruantang.service.permissions.model.request.VersionQueryRequest;
import com.ruantang.service.permissions.model.request.VersionUpdateRequest;

/**
 * 配置权限版本服务接口
 */
public interface ConfigPermVersionService {
    
    /**
     * 分页查询配置权限版本
     * @param request 查询请求参数
     * @return 分页结果
     */
    ApiResult<Page<ConfigPermVersionDTO>> queryVersionPage(VersionQueryRequest request);
    
    /**
     * 根据ID获取配置权限版本详情
     * @param id 版本ID
     * @return 版本详情
     */
    ApiResult<ConfigPermVersionDTO> getVersionById(Long id);
    
    /**
     * 根据模板ID获取默认版本信息
     * @param templateId 模板ID
     * @return 默认版本信息
     */
    ApiResult<ConfigPermVersionDTO> getDefaultVersionByTemplateId(Long templateId);
    
    /**
     * 创建配置权限版本
     * @param request 创建请求参数
     * @return 创建的版本信息
     */
    ApiResult<ConfigPermVersionDTO> createVersion(VersionCreateRequest request);
    
    /**
     * 更新配置权限版本
     * @param request 更新请求参数
     * @return 更新结果
     */
    ApiResult<Boolean> updateVersion(VersionUpdateRequest request);
    
    /**
     * 删除配置权限版本
     * @param id 版本ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteVersionById(Long id);
}
