package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.user.model.dto.PermDataPolicyDTO;
import com.ruantang.service.user.model.request.PolicyCreateRequest;
import com.ruantang.service.user.model.request.PolicyQueryRequest;
import com.ruantang.service.user.model.request.PolicyUpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * 数据权限策略服务接口
 */
public interface PermDataPolicyService {
    
    /**
     * 分页查询数据权限策略
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<PermDataPolicyDTO>> queryPolicyPage(PolicyQueryRequest request);
    
    /**
     * 根据ID查询数据权限策略
     * 
     * @param id 策略ID
     * @return 数据权限策略详情
     */
    ApiResult<PermDataPolicyDTO> getPolicyById(Long id);
    
    /**
     * 创建数据权限策略
     * 
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<PermDataPolicyDTO> createPolicy(PolicyCreateRequest request);
    
    /**
     * 更新数据权限策略
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updatePolicy(PolicyUpdateRequest request);
    
    /**
     * 删除数据权限策略
     * 
     * @param id 策略ID
     * @return 删除结果
     */
    ApiResult<Boolean> deletePolicyById(Long id);
    
    /**
     * 根据角色ID列表查询数据权限策略列表
     * 
     * @param roleIds 角色ID列表
     * @return 数据权限策略列表
     */
    List<PermDataPolicyDTO> getPoliciesByRoleIds(List<Long> roleIds);
    
    /**
     * 根据租户ID查询数据权限策略列表
     * 
     * @param tenantId 租户ID
     * @return 数据权限策略列表
     */
    List<PermDataPolicyDTO> getPoliciesByTenantId(Long tenantId);
    
    /**
     * 获取用户所有数据权限策略Map
     * 
     * @param userId 用户ID
     * @return 数据权限策略Map，key为表名，value为策略对象
     */
    Map<String, PermDataPolicyDTO> getUserDataPolicies(Long userId);
} 