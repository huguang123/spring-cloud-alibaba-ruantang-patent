package com.ruantang.service.organization.repository;

import com.ruantang.entity.organ.OrganizationHierarchy;

import java.util.List;

/**
 * 组织层级关系仓库接口
 */
public interface OrganizationHierarchyRepository {
    
    /**
     * 批量保存组织层级关系
     *
     * @param hierarchies 组织层级关系列表
     * @return 是否成功
     */
    boolean batchSave(List<OrganizationHierarchy> hierarchies);
    
    /**
     * 初始化组织层级关系
     *
     * @param orgId 组织ID
     * @param parentId 父组织ID
     * @return 是否成功
     */
    boolean initHierarchy(Long orgId, Long parentId);
    
    /**
     * 删除与组织相关的所有层级关系
     *
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean removeByOrgId(Long orgId);
    
    /**
     * Ò找某个组织的所有祖先节点ID
     *
     * @param orgId 组织ID
     * @return 祖先组织ID列表
     */
    List<Long> findAncestors(Long orgId);
    
    /**
     * 查找某个组织的所有子孙节点ID
     *
     * @param orgId 组织ID
     * @return 子孙组织ID列表
     */
    List<Long> findDescendants(Long orgId);
    
    /**
     * 查找某个组织的直接子节点ID
     *
     * @param orgId 组织ID
     * @return 直接子节点组织ID列表
     */
    List<Long> findDirectChildren(Long orgId);
    
    /**
     * 检查是否有子组织
     *
     * @param orgId 组织ID
     * @return 是否有子组织
     */
    boolean hasChildren(Long orgId);
} 