package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruantang.entity.perm.PermRelPolicyBinding;
import com.ruantang.mapper.perm.PermRelPolicyBindingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据权限策略绑定关系数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class PermRelPolicyBindingRepository {

    private final PermRelPolicyBindingMapper policyBindingMapper;
    
    /**
     * 查询角色的策略绑定列表
     */
    public List<PermRelPolicyBinding> listRolePolicyBindings(Long roleId) {
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getBindType, 1) // 1代表角色
                .eq(PermRelPolicyBinding::getBindId, roleId);
        return policyBindingMapper.selectList(queryWrapper);
    }
    
    /**
     * 查询策略是否已绑定到角色
     */
    public boolean isPolicyBindToRole(Long policyId, Long roleId) {
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getPolicyId, policyId)
                .eq(PermRelPolicyBinding::getBindType, 1) // 1代表角色
                .eq(PermRelPolicyBinding::getBindId, roleId);
        return policyBindingMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 绑定策略到角色
     */
    public boolean bindPolicyToRole(Long policyId, Long roleId) {
        // 先检查是否已经绑定
        if (isPolicyBindToRole(policyId, roleId)) {
            return true;
        }
        
        PermRelPolicyBinding binding = new PermRelPolicyBinding();
        binding.setPolicyId(policyId);
        binding.setBindType(1); // 1代表角色
        binding.setBindId(roleId);
        binding.setCreateTime(System.currentTimeMillis());
        
        return policyBindingMapper.insert(binding) > 0;
    }
    
    /**
     * 批量绑定策略到角色
     */
    public boolean batchBindPoliciesToRole(List<Long> policyIds, Long roleId) {
        if (policyIds == null || policyIds.isEmpty()) {
            return true;
        }
        
        for (Long policyId : policyIds) {
            bindPolicyToRole(policyId, roleId);
        }
        
        return true;
    }
    
    /**
     * 解绑角色的所有策略
     */
    public boolean unbindAllPoliciesFromRole(Long roleId) {
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getBindType, 1) // 1代表角色
                .eq(PermRelPolicyBinding::getBindId, roleId);
        
        return policyBindingMapper.delete(queryWrapper) >= 0;
    }
    
    /**
     * 解绑角色的特定策略
     */
    public boolean unbindPolicyFromRole(Long policyId, Long roleId) {
        LambdaQueryWrapper<PermRelPolicyBinding> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermRelPolicyBinding::getPolicyId, policyId)
                .eq(PermRelPolicyBinding::getBindType, 1) // 1代表角色
                .eq(PermRelPolicyBinding::getBindId, roleId);
        
        return policyBindingMapper.delete(queryWrapper) >= 0;
    }
} 