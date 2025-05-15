package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.request.PolicyCreateRequest;
import com.ruantang.service.permissions.model.request.PolicyQueryRequest;
import com.ruantang.service.permissions.model.request.PolicyUpdateRequest;
import com.ruantang.service.permissions.repository.PermDataPolicyRepository;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import com.ruantang.service.permissions.util.PermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据权限策略服务实现类
 */
@Service
@RequiredArgsConstructor
public class PermDataPolicyServiceImpl implements PermDataPolicyService {

    private final PermDataPolicyRepository policyRepository;

    @Override
    public ApiResult<Page<PermDataPolicyDTO>> queryPolicyPage(PolicyQueryRequest request) {
        Page<PermDataPolicy> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<PermDataPolicy> policyPage = policyRepository.queryPolicyPage(
                request.getPolicyName(),
                request.getPolicyCode(),
                request.getConditionType(),
                page
        );
        
        // 转换为DTO
        Page<PermDataPolicyDTO> resultPage = new Page<>(policyPage.getCurrent(), policyPage.getSize(), policyPage.getTotal());
        List<PermDataPolicyDTO> records = PermUtil.convertToPolicyDTOList(policyPage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<PermDataPolicyDTO> getPolicyById(Long id) {
        PermDataPolicy policy = policyRepository.getPolicyById(id);
        if (policy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        return ApiResult.success(PermUtil.convertToPolicyDTO(policy));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<PermDataPolicyDTO> createPolicy(PolicyCreateRequest request) {
        // 检查策略编码是否已存在
        PermDataPolicy existPolicy = policyRepository.getPolicyByCode(request.getPolicyCode());
        if (existPolicy != null) {
            throw new ApiException("策略编码已存在");
        }
        
        PermDataPolicy policy = new PermDataPolicy();
        BeanUtils.copyProperties(request, policy);
        
        // 设置创建时间
        policy.setCreateTime(System.currentTimeMillis());
        policy.setUpdateTime(System.currentTimeMillis());
        
        // 生成ID (这里使用时间戳，实际应用中可以使用雪花算法等)
//        policy.setId(System.currentTimeMillis());
        
        boolean success = policyRepository.savePolicy(policy);
        if (!success) {
            return ApiResult.failed("创建数据权限策略失败");
        }
        
        return ApiResult.success(PermUtil.convertToPolicyDTO(policy));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updatePolicy(PolicyUpdateRequest request) {
        // 检查策略是否存在
        PermDataPolicy existPolicy = policyRepository.getPolicyById(request.getId());
        if (existPolicy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        // 检查策略编码是否重复
        if (request.getPolicyCode() != null && !request.getPolicyCode().equals(existPolicy.getPolicyCode())) {
            PermDataPolicy codeExistPolicy = policyRepository.getPolicyByCode(request.getPolicyCode());
            if (codeExistPolicy != null) {
                return ApiResult.failed("策略编码已存在");
            }
        }
        
        PermDataPolicy policy = new PermDataPolicy();
        BeanUtils.copyProperties(request, policy);
        
        // 设置更新时间
        policy.setUpdateTime(System.currentTimeMillis());
        
        boolean success = policyRepository.updatePolicy(policy);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deletePolicyById(Long id) {
        // 检查策略是否存在
        PermDataPolicy existPolicy = policyRepository.getPolicyById(id);
        if (existPolicy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        boolean success = policyRepository.deletePolicyById(id);
        return ApiResult.success(success);
    }
} 