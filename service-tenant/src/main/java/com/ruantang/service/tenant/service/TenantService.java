package com.ruantang.service.tenant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.tenant.model.dto.TenantDTO;
import com.ruantang.service.tenant.model.dto.TenantRoleDTO;
import com.ruantang.service.tenant.model.dto.TenantRolePermissionDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateBindDTO;
import com.ruantang.service.tenant.model.request.TenantBindTemplatesRequest;
import com.ruantang.service.tenant.model.request.TenantCreateRequest;
import com.ruantang.service.tenant.model.request.TenantQueryRequest;
import com.ruantang.service.tenant.model.request.TenantRoleVerifyRequest;
import com.ruantang.service.tenant.model.request.TenantUpdateRequest;
import com.ruantang.service.tenant.model.request.TenantUserRolesRequest;

import java.util.List;

/**
 * 租户服务接口
 */
public interface TenantService {
    
    /**
     * 分页查询租户
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<TenantDTO>> queryTenantPage(TenantQueryRequest request);
    
    /**
     * 根据ID查询租户详情
     * 
     * @param id 租户ID
     * @return 租户详情
     */
    ApiResult<TenantDTO> getTenantById(Long id);
    
    /**
     * 创建租户
     * 
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<TenantDTO> createTenant(TenantCreateRequest request);
    
    /**
     * 更新租户
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updateTenant(TenantUpdateRequest request);
    
    /**
     * 删除租户
     * 
     * @param id 租户ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteTenantById(Long id);
    
    /**
     * 绑定模板到租户
     * 
     * @param request 绑定请求
     * @return 绑定结果
     */
    ApiResult<Boolean> bindTenantTemplates(TenantBindTemplatesRequest request);
    
    /**
     * 解绑租户与模板的关系
     * 
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @return 解绑结果
     */
    ApiResult<Boolean> unbindTenantTemplate(Long tenantId, Long templateId);
    
    /**
     * 根据租户ID查询绑定的模板列表
     * 
     * @param tenantId 租户ID
     * @return 模板列表
     */
    ApiResult<List<TenantTemplateBindDTO>> getTenantTemplates(Long tenantId);
    
    /**
     * 根据租户ID查询绑定的角色列表
     * 
     * @param tenantId 租户ID
     * @return 角色列表
     */
    ApiResult<List<TenantRoleDTO>> getTenantRoles(Long tenantId);
    
    /**
     * 获取用户在特定租户下可以分配的角色权限
     * 
     * @param request 查询请求
     * @return 用户可分配的角色列表
     */
    ApiResult<List<TenantRoleDTO>> getUserAssignableTenantRoles(TenantUserRolesRequest request);
    
    /**
     * 验证租户角色权限状态
     * 
     * @param request 验证请求
     * @return 角色权限信息列表
     */
    ApiResult<List<TenantRolePermissionDTO>> verifyTenantRolePermissions(TenantRoleVerifyRequest request);
} 