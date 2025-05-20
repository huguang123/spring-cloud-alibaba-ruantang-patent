package com.ruantang.service.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.entity.perm.PermRelPolicyBinding;
import com.ruantang.mapper.perm.PermDataPolicyMapper;
import com.ruantang.mapper.perm.PermRelPolicyBindingMapper;
import com.ruantang.service.user.repository.PermDataPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限策略数据访问仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class PermDataPolicyRepositoryImpl implements PermDataPolicyRepository {
    
    private final PermDataPolicyMapper policyMapper;
    private final PermRelPolicyBindingMapper policyBindingMapper;
    
    @Override
    public Page<PermDataPolicy> queryPolicyPage(String policyCode, String policyName, Integer conditionType, Page<PermDataPolicy> page) {
        LambdaQueryWrapper<PermDataPolicy> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        if (StringUtils.hasText(policyCode)) {
            queryWrapper.like(PermDataPolicy::getPolicyCode, policyCode);
        }
        
        if (StringUtils.hasText(policyName)) {
            queryWrapper.like(PermDataPolicy::getPolicyName, policyName);
        }
        
        if (conditionType != null) {
            queryWrapper.eq(PermDataPolicy::getConditionType, conditionType);
        }
        
        // 按优先级降序和创建时间降序排序
        queryWrapper.orderByDesc(PermDataPolicy::getPriority)
                   .orderByDesc(PermDataPolicy::getCreateTime);
        
        return policyMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public PermDataPolicy getPolicyById(Long id) {
        return policyMapper.selectById(id);
    }
    
    @Override
    public PermDataPolicy getPolicyByCode(String policyCode) {
        if (!StringUtils.hasText(policyCode)) {
            return null;
        }
        
        LambdaQueryWrapper<PermDataPolicy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDataPolicy::getPolicyCode, policyCode);
        
        return policyMapper.selectOne(queryWrapper);
    }
    
    @Override
    public boolean createPolicy(PermDataPolicy policy) {
        return policyMapper.insert(policy) > 0;
    }
    
    @Override
    public boolean updatePolicy(PermDataPolicy policy) {
        return policyMapper.updateById(policy) > 0;
    }
    
    @Override
    public boolean deletePolicyById(Long id) {
        return policyMapper.deleteById(id) > 0;
    }
    
    @Override
    public List<PermDataPolicy> getPoliciesByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 查询角色绑定的策略
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getBindType, 1) // 1:角色
                   .in(PermRelPolicyBinding::getBindId, roleIds);
        
        List<PermRelPolicyBinding> policyBindings = policyBindingMapper.selectList(queryWrapper);
        
        if (CollectionUtils.isEmpty(policyBindings)) {
            return Collections.emptyList();
        }
        
        // 获取策略ID列表
        List<Long> policyIds = policyBindings.stream()
                .map(PermRelPolicyBinding::getPolicyId)
                .distinct()
                .collect(Collectors.toList());
        
        // 查询策略列表
        return policyMapper.selectBatchIds(policyIds);
    }
    
    @Override
    public List<PermDataPolicy> getPoliciesByTenantId(Long tenantId) {
        if (tenantId == null) {
            return Collections.emptyList();
        }
        
        // 查询租户绑定的策略
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getBindType, 2) // 2:租户
                   .eq(PermRelPolicyBinding::getBindId, tenantId);
        
        List<PermRelPolicyBinding> policyBindings = policyBindingMapper.selectList(queryWrapper);
        
        if (CollectionUtils.isEmpty(policyBindings)) {
            return Collections.emptyList();
        }
        
        // 获取策略ID列表
        List<Long> policyIds = policyBindings.stream()
                .map(PermRelPolicyBinding::getPolicyId)
                .distinct()
                .collect(Collectors.toList());
        
        // 查询策略列表
        return policyMapper.selectBatchIds(policyIds);
    }
} 