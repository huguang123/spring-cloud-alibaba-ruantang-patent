package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.mapper.perm.PermDataPolicyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * 数据权限策略数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class PermDataPolicyRepository {

    private final PermDataPolicyMapper policyMapper;
    
    /**
     * 分页查询数据权限策略
     */
    public Page<PermDataPolicy> queryPolicyPage(String policyName, String policyCode, Integer conditionType, Page<PermDataPolicy> page) {
        LambdaQueryWrapper<PermDataPolicy> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(policyName)) {
            queryWrapper.like(PermDataPolicy::getPolicyName, policyName);
        }
        
        if (StringUtils.hasText(policyCode)) {
            queryWrapper.like(PermDataPolicy::getPolicyCode, policyCode);
        }
        
        if (conditionType != null) {
            queryWrapper.eq(PermDataPolicy::getConditionType, conditionType);
        }
        
        queryWrapper.orderByDesc(PermDataPolicy::getPriority);
        return policyMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询数据权限策略
     */
    public PermDataPolicy getPolicyById(Long id) {
        return policyMapper.selectById(id);
    }
    
    /**
     * 根据策略编码查询数据权限策略
     */
    public PermDataPolicy getPolicyByCode(String policyCode) {
        LambdaQueryWrapper<PermDataPolicy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDataPolicy::getPolicyCode, policyCode);
        return policyMapper.selectOne(queryWrapper);
    }
    
    /**
     * 保存数据权限策略
     */
    public boolean savePolicy(PermDataPolicy policy) {
        return policyMapper.insert(policy) > 0;
    }
    
    /**
     * 更新数据权限策略
     */
    public boolean updatePolicy(PermDataPolicy policy) {
        return policyMapper.updateById(policy) > 0;
    }
    
    /**
     * 删除数据权限策略
     */
    public boolean deletePolicyById(Long id) {
        return policyMapper.deleteById(id) > 0;
    }
} 