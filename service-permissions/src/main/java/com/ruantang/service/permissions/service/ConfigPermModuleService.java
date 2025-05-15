package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermModuleDTO;
import com.ruantang.service.permissions.model.request.ModuleCreateRequest;
import com.ruantang.service.permissions.model.request.ModuleQueryRequest;
import com.ruantang.service.permissions.model.request.ModuleUpdateRequest;
import java.util.List;

/**
 * 配置权限模块服务接口
 */
public interface ConfigPermModuleService {
    
    /**
     * 分页查询配置权限模块
     * @param request 查询请求参数
     * @return 分页结果
     */
    ApiResult<Page<ConfigPermModuleDTO>> queryModulePage(ModuleQueryRequest request);
    
    /**
     * 根据ID获取配置权限模块详情
     * @param id 模块ID
     * @return 模块详情
     */
    ApiResult<ConfigPermModuleDTO> getModuleById(Long id);
    
    /**
     * 根据版本ID获取模块列表
     * @param templateVersionId 模板版本ID
     * @return 模块列表
     */
    ApiResult<List<ConfigPermModuleDTO>> listModulesByVersionId(Long templateVersionId);
    
    /**
     * 创建配置权限模块
     * @param request 创建请求参数
     * @return 创建的模块信息
     */
    ApiResult<ConfigPermModuleDTO> createModule(ModuleCreateRequest request);
    
    /**
     * 更新配置权限模块
     * @param request 更新请求参数
     * @return 更新结果
     */
    ApiResult<Boolean> updateModule(ModuleUpdateRequest request);
    
    /**
     * 删除配置权限模块
     * @param id 模块ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteModuleById(Long id);
} 