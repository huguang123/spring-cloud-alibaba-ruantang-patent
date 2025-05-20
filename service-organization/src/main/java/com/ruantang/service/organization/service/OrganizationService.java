package com.ruantang.service.organization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.organization.model.dto.OrganizationDTO;
import com.ruantang.service.organization.model.dto.OrganizationTreeDTO;
import com.ruantang.service.organization.model.request.OrganizationCreateRequest;
import com.ruantang.service.organization.model.request.OrganizationQueryRequest;
import com.ruantang.service.organization.model.request.OrganizationUpdateRequest;

import java.util.List;

/**
 * 组织服务接口
 */
public interface OrganizationService {
    
    /**
     * 获取组织树结构
     *
     * @param tenantId 租户ID
     * @return 组织树结构
     */
    ApiResult<List<OrganizationTreeDTO>> getOrganizationTree(Long tenantId);
    
    /**
     * 分页查询组织列表
     *
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<OrganizationDTO>> queryOrganizationPage(OrganizationQueryRequest request);
    
    /**
     * 根据ID查询组织详情
     *
     * @param id 组织ID
     * @return 组织详情
     */
    ApiResult<OrganizationDTO> getOrganizationById(Long id);
    
    /**
     * 创建组织
     *
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<OrganizationDTO> createOrganization(OrganizationCreateRequest request);
    
    /**
     * 更新组织
     *
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updateOrganization(OrganizationUpdateRequest request);
    
    /**
     * 删除组织
     *
     * @param id 组织ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteOrganization(Long id);
} 