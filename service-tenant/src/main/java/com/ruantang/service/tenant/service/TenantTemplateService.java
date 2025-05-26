package com.ruantang.service.tenant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.tenant.model.dto.SysRolesDTO;
import com.ruantang.service.tenant.model.dto.TemplateRoleDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateDTO;
import com.ruantang.service.tenant.model.request.TemplateCreateRequest;
import com.ruantang.service.tenant.model.request.TemplateQueryRequest;
import com.ruantang.service.tenant.model.request.TemplateRoleBindRequest;
import com.ruantang.service.tenant.model.request.TemplateUpdateRequest;

import java.util.List;

/**
 * 租户模板服务接口
 */
public interface TenantTemplateService {

    /**
     * 分页查询租户模板
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<TenantTemplateDTO>> queryTemplatePage(TemplateQueryRequest request);

    /**
     * 根据ID查询租户模板详情（包含关联角色信息）
     * 
     * @param id 模板ID
     * @return 模板详情
     */
    ApiResult<TenantTemplateDTO> getTemplateById(Long id);

    /**
     * 创建租户模板
     * 
     * @param request 创建请求
     * @return 创建后的模板
     */
    ApiResult<TenantTemplateDTO> createTemplate(TemplateCreateRequest request);

    /**
     * 更新租户模板
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updateTemplate(TemplateUpdateRequest request);

    /**
     * 删除租户模板
     * 
     * @param id 模板ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteTemplateById(Long id);

    /**
     * 为模板绑定角色
     * 
     * @param request 角色绑定请求
     * @return 绑定结果
     */
    ApiResult<Boolean> bindTemplateRoles(TemplateRoleBindRequest request);
    
    /**
     * 根据模板ID查询绑定的角色列表
     *
     * @param id 模板ID
     * @return 角色列表
     */
    ApiResult<List<TemplateRoleDTO>> getTemplateRoles(Long id);
} 