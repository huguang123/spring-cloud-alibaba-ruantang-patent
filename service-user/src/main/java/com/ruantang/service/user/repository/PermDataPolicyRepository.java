package com.ruantang.service.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.PermDataPolicy;

import java.util.List;

/**
 * 数据权限策略数据访问仓库接口
 */
public interface PermDataPolicyRepository {
    
    /**
     * 分页查询数据权限策略
     * 
     * @param policyCode 策略编码
     * @param policyName 策略名称
     * @param conditionType 条件类型
     * @param page 分页对象
     * @return 分页结果
     */
    Page<PermDataPolicy> queryPolicyPage(String policyCode, String policyName, Integer conditionType, Page<PermDataPolicy> page);
    
    /**
     * 根据ID查询数据权限策略
     * 
     * @param id 策略ID
     * @return 数据权限策略
     */
    PermDataPolicy getPolicyById(Long id);
    
    /**
     * 根据策略编码查询数据权限策略
     * 
     * @param policyCode 策略编码
     * @return 数据权限策略
     */
    PermDataPolicy getPolicyByCode(String policyCode);
    
    /**
     * 创建数据权限策略
     * 
     * @param policy 数据权限策略
     * @return 是否成功
     */
    boolean createPolicy(PermDataPolicy policy);
    
    /**
     * 更新数据权限策略
     * 
     * @param policy 数据权限策略
     * @return 是否成功
     */
    boolean updatePolicy(PermDataPolicy policy);
    
    /**
     * 删除数据权限策略
     * 
     * @param id 策略ID
     * @return 是否成功
     */
    boolean deletePolicyById(Long id);
    
    /**
     * 根据角色ID列表查询数据权限策略列表
     * 
     * @param roleIds 角色ID列表
     * @return 数据权限策略列表
     */
    List<PermDataPolicy> getPoliciesByRoleIds(List<Long> roleIds);
    
    /**
     * 根据租户ID查询数据权限策略列表
     * 
     * @param tenantId 租户ID
     * @return 数据权限策略列表
     */
    List<PermDataPolicy> getPoliciesByTenantId(Long tenantId);
    
    /**
     * 根据数据策略ID列表查询数据权限策略列表
     * 
     * @param policyIds 数据策略ID列表
     * @return 数据权限策略列表
     */
    List<PermDataPolicy> getPoliciesByIds(List<Long> policyIds);
} 