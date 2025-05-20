package com.ruantang.service.organization.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.organ.Organization;

import java.util.List;
import java.util.Map;

/**
 * 组织仓库接口
 */
public interface OrganizationRepository {
    
    /**
     * 分页查询组织列表
     *
     * @param orgName 组织名称
     * @param orgCode 组织编码
     * @param tenantId 租户ID
     * @param state 状态
     * @param page 分页参数
     * @return 分页结果
     */
    Page<Organization> queryPage(String orgName, String orgCode, Long tenantId, Integer state, Page<Organization> page);
    
    /**
     * 根据ID查询组织
     *
     * @param id 组织ID
     * @return 组织信息
     */
    Organization getById(Long id);
    
    /**
     * 根据组织编码查询组织
     *
     * @param orgCode 组织编码
     * @param tenantId 租户ID
     * @return 组织信息
     */
    Organization getByCode(String orgCode, Long tenantId);
    
    /**
     * 根据租户ID查询所有组织
     *
     * @param tenantId 租户ID
     * @return 组织列表
     */
    List<Organization> listByTenantId(Long tenantId);
    
    /**
     * 创建组织
     *
     * @param organization 组织信息
     * @return 是否成功
     */
    boolean save(Organization organization);
    
    /**
     * 更新组织
     *
     * @param organization 组织信息
     * @return 是否成功
     */
    boolean update(Organization organization);
    
    /**
     * 删除组织
     *
     * @param id 组织ID
     * @return 是否成功
     */
    boolean remove(Long id);
    
    /**
     * 查询组织的成员数量
     *
     * @param orgIds 组织ID列表
     * @return 组织ID到成员数量的映射
     */
    Map<Long, Integer> getMemberCounts(List<Long> orgIds);
    
    /**
     * 查询是否有用户绑定到指定组织
     *
     * @param orgId 组织ID
     * @return 是否有用户绑定
     */
    boolean hasBindUser(Long orgId);
} 