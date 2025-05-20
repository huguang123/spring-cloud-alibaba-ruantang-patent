package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.service.user.model.dto.PermDataPolicyDTO;
import com.ruantang.service.user.model.request.PolicyCreateRequest;
import com.ruantang.service.user.model.request.PolicyQueryRequest;
import com.ruantang.service.user.model.request.PolicyUpdateRequest;
import com.ruantang.service.user.repository.PermDataPolicyRepository;
import com.ruantang.service.user.service.PermDataPolicyService;
import com.ruantang.service.user.util.PermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限策略服务实现类
 */
@Service
@RequiredArgsConstructor
public class PermDataPolicyServiceImpl implements PermDataPolicyService {
    
    private final PermDataPolicyRepository policyRepository;
    private final RelUserRolesMapper relUserRolesMapper;
    
    @Override
    public ApiResult<Page<PermDataPolicyDTO>> queryPolicyPage(PolicyQueryRequest request) {
        Page<PermDataPolicy> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<PermDataPolicy> policyPage = policyRepository.queryPolicyPage(
                request.getPolicyCode(),
                request.getPolicyName(),
                request.getConditionType(),
                page
        );
        
        Page<PermDataPolicyDTO> resultPage = new Page<>(policyPage.getCurrent(), policyPage.getSize(), policyPage.getTotal());
        List<PermDataPolicyDTO> policyDTOList = policyPage.getRecords().stream()
                .map(PermUtil::convertToDTO)
                .collect(Collectors.toList());
        resultPage.setRecords(policyDTOList);
        
        return ApiResult.success(resultPage);
    }
    
    @Override
    public ApiResult<PermDataPolicyDTO> getPolicyById(Long id) {
        PermDataPolicy policy = policyRepository.getPolicyById(id);
        if (policy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        return ApiResult.success(PermUtil.convertToDTO(policy));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<PermDataPolicyDTO> createPolicy(PolicyCreateRequest request) {
        // 检查策略编码是否存在
        PermDataPolicy existingPolicy = policyRepository.getPolicyByCode(request.getPolicyCode());
        if (existingPolicy != null) {
            return ApiResult.failed("策略编码已存在");
        }
        
        // 创建策略
        PermDataPolicy policy = new PermDataPolicy();
        BeanUtils.copyProperties(request, policy);
        policy.setCreateTime(System.currentTimeMillis());
        policy.setUpdateTime(System.currentTimeMillis());
        
        boolean success = policyRepository.createPolicy(policy);
        if (!success) {
            return ApiResult.failed("创建数据权限策略失败");
        }
        
        return ApiResult.success(PermUtil.convertToDTO(policy));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updatePolicy(PolicyUpdateRequest request) {
        // 检查策略是否存在
        PermDataPolicy existingPolicy = policyRepository.getPolicyById(request.getId());
        if (existingPolicy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        // 如果更新策略编码，检查是否与其他策略冲突
        if (request.getPolicyCode() != null && !request.getPolicyCode().equals(existingPolicy.getPolicyCode())) {
            PermDataPolicy policyByCode = policyRepository.getPolicyByCode(request.getPolicyCode());
            if (policyByCode != null && !policyByCode.getId().equals(request.getId())) {
                return ApiResult.failed("策略编码已被其他策略使用");
            }
        }
        
        // 更新策略
        PermDataPolicy policy = new PermDataPolicy();
        BeanUtils.copyProperties(request, policy);
        policy.setUpdateTime(System.currentTimeMillis());
        
        boolean success = policyRepository.updatePolicy(policy);
        if (!success) {
            return ApiResult.failed("更新数据权限策略失败");
        }
        
        return ApiResult.success(true);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deletePolicyById(Long id) {
        // 检查策略是否存在
        PermDataPolicy existingPolicy = policyRepository.getPolicyById(id);
        if (existingPolicy == null) {
            return ApiResult.failed("数据权限策略不存在");
        }
        
        // 删除策略
        boolean success = policyRepository.deletePolicyById(id);
        if (!success) {
            return ApiResult.failed("删除数据权限策略失败");
        }
        
        return ApiResult.success(true);
    }
    
    @Override
    public List<PermDataPolicyDTO> getPoliciesByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        List<PermDataPolicy> policies = policyRepository.getPoliciesByRoleIds(roleIds);
        return policies.stream()
                .map(PermUtil::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PermDataPolicyDTO> getPoliciesByTenantId(Long tenantId) {
        if (tenantId == null) {
            return Collections.emptyList();
        }
        
        List<PermDataPolicy> policies = policyRepository.getPoliciesByTenantId(tenantId);
        return policies.stream()
                .map(PermUtil::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, PermDataPolicyDTO> getUserDataPolicies(Long userId) {
        if (userId == null) {
            return Collections.emptyMap();
        }
        
        // 获取用户角色
        List<Long> roleIds = getUserRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }
        
        // 获取角色绑定的数据权限策略
        List<PermDataPolicy> rolePolicies = policyRepository.getPoliciesByRoleIds(roleIds);
        
        // 构建数据权限策略Map
        Map<String, PermDataPolicyDTO> policyMap = new HashMap<>();
        
        // 处理角色数据权限策略
        processPolicies(rolePolicies, policyMap);
        
        return policyMap;
    }
    
    /**
     * 处理数据权限策略列表，转换为以表名为key的Map
     *
     * @param policies 策略列表
     * @param policyMap 策略Map
     */
    private void processPolicies(List<PermDataPolicy> policies, Map<String, PermDataPolicyDTO> policyMap) {
        if (CollectionUtils.isEmpty(policies)) {
            return;
        }
        
        // 对策略按优先级排序（优先级高的先处理）
        policies.sort(Comparator.comparing(PermDataPolicy::getPriority, Comparator.nullsLast(Comparator.reverseOrder())));
        
        for (PermDataPolicy policy : policies) {
            if (policy.getEffectTables() == null) {
                continue;
            }
            
            // 分割生效表
            String[] tables = policy.getEffectTables().split(",");
            for (String table : tables) {
                String tableName = table.trim();
                if (!tableName.isEmpty() && !policyMap.containsKey(tableName)) {
                    // 如果该表还没有策略，添加该策略
                    policyMap.put(tableName, PermUtil.convertToDTO(policy));
                }
            }
        }
    }
    
    /**
     * 获取用户的角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    private List<Long> getUserRoleIds(Long userId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RelUserRoles> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(RelUserRoles::getUserId, userId);
        
        List<RelUserRoles> userRoles = relUserRolesMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        
        return userRoles.stream()
                .map(RelUserRoles::getRoleId)
                .collect(Collectors.toList());
    }
} 