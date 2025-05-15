package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.service.permissions.model.dto.ConfigPermTemplateDTO;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.request.TemplateCreateRequest;
import com.ruantang.service.permissions.model.request.TemplateQueryRequest;
import com.ruantang.service.permissions.model.request.TemplateUpdateRequest;

/**
 * 配置权限模板服务接口
 */
public interface ConfigPermTemplateService {
    
    /**
     * 分页查询配置权限模板
     * @param request 查询请求参数
     * @return 分页结果
     */
    ApiResult<Page<ConfigPermTemplateDTO>> queryTemplatePage(TemplateQueryRequest request);
    
    /**
     * 根据ID获取配置权限模板详情
     * @param id 模板ID
     * @return 模板详情
     */
    ApiResult<ConfigPermTemplateDTO> getTemplateById(Long id);
    
    /**
     * 根据模板类型获取配置权限模板
     * @param templateType 模板类型(1=平台配置模板 2=企业租户配置模板 3=代理所配置模板)
     * @return 模板信息
     */
    ApiResult<ConfigPermTemplateDTO> getTemplateByType(Integer templateType);
    
    /**
     * 创建配置权限模板
     * @param request 创建请求参数
     * @return 创建的模板信息
     */
    ApiResult<ConfigPermTemplateDTO> createTemplate(TemplateCreateRequest request);
    
    /**
     * 更新配置权限模板
     * @param request 更新请求参数
     * @return 更新结果
     */
    ApiResult<Boolean> updateTemplate(TemplateUpdateRequest request);
    
    /**
     * 删除配置权限模板
     * @param id 模板ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteTemplateById(Long id);
    
    /**
     * 根据角色类型获取对应模板的默认版本
     * 角色类型(1:平台角色 2:企业角色 3:代理所角色)将映射到
     * 模板类型(1=平台配置模板 2=企业租户配置模板 3=代理所配置模板)
     * @param roleType 角色类型
     * @return 模板默认版本
     */
    ApiResult<ConfigPermVersion> getDefaultVersionByRoleType(Integer roleType);
    
    /**
     * 根据角色类型获取权限树
     * @param roleType 角色类型
     * @return 权限树
     */
    ApiResult<PermNodeTreeDTO> getPermTreeByRoleType(Integer roleType);
}
