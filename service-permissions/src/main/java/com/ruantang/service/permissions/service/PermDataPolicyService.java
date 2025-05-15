package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.request.PolicyCreateRequest;
import com.ruantang.service.permissions.model.request.PolicyQueryRequest;
import com.ruantang.service.permissions.model.request.PolicyUpdateRequest;

/**
 * 数据权限策略服务接口
 */
public interface PermDataPolicyService {
    
    /**
     * 分页查询数据权限策略
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<PermDataPolicyDTO>> queryPolicyPage(PolicyQueryRequest request);
    
    /**
     * 根据ID查询数据权限策略
     * @param id 策略ID
     * @return 策略信息
     */
    ApiResult<PermDataPolicyDTO> getPolicyById(Long id);
    
    /**
     * 创建数据权限策略
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<PermDataPolicyDTO> createPolicy(PolicyCreateRequest request);
    
    /**
     * 更新数据权限策略
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updatePolicy(PolicyUpdateRequest request);
    
    /**
     * 删除数据权限策略
     * @param id 策略ID
     * @return 删除结果
     */
    ApiResult<Boolean> deletePolicyById(Long id);
} 