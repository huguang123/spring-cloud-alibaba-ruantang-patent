package com.ruantang.service.organization.repository.impl;

import com.ruantang.entity.organ.OrganizationHierarchy;
import com.ruantang.mapper.organ.OrganizationHierarchyMapper;
import com.ruantang.service.organization.repository.OrganizationHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织层级关系仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class OrganizationHierarchyRepositoryImpl implements OrganizationHierarchyRepository {
    
    private final OrganizationHierarchyMapper hierarchyMapper;
    
    @Override
    public boolean batchSave(List<OrganizationHierarchy> hierarchies) {
        if (hierarchies == null || hierarchies.isEmpty()) {
            return false;
        }
        
        return hierarchyMapper.batchInsert(hierarchies) > 0;
    }
    
    @Override
    public boolean initHierarchy(Long orgId, Long parentId) {
        if (orgId == null) {
            return false;
        }
        
        List<OrganizationHierarchy> hierarchies = new ArrayList<>();
        
        // 添加自身到自身的关系（深度为0）
        OrganizationHierarchy selfRel = new OrganizationHierarchy();
        selfRel.setAncestor(orgId);
        selfRel.setDescendant(orgId);
        selfRel.setDepth(0);
        hierarchies.add(selfRel);
        
        // 如果有父组织，则需要添加祖先关系
        if (parentId != null && parentId > 0) {
            // 获取父组织的所有祖先节点
            List<Long> ancestors = hierarchyMapper.findAncestors(parentId);
            
            // 添加所有祖先到当前节点的关系
            int depth = 1;
            for (Long ancestor : ancestors) {
                OrganizationHierarchy ancestorRel = new OrganizationHierarchy();
                ancestorRel.setAncestor(ancestor);
                ancestorRel.setDescendant(orgId);
                ancestorRel.setDepth(depth);
                hierarchies.add(ancestorRel);
                
                if (ancestor.equals(parentId)) {
                    // 确保父节点的深度为1
                    ancestorRel.setDepth(1);
                } else {
                    // 其他祖先节点的深度根据关系确定
                    ancestorRel.setDepth(depth++);
                }
            }
        }
        
        return batchSave(hierarchies);
    }
    
    @Override
    public boolean removeByOrgId(Long orgId) {
        if (orgId == null) {
            return false;
        }
        
        return hierarchyMapper.deleteByOrgId(orgId) > 0;
    }
    
    @Override
    public List<Long> findAncestors(Long orgId) {
        if (orgId == null) {
            return new ArrayList<>();
        }
        
        return hierarchyMapper.findAncestors(orgId);
    }
    
    @Override
    public List<Long> findDescendants(Long orgId) {
        if (orgId == null) {
            return new ArrayList<>();
        }
        
        return hierarchyMapper.findDescendants(orgId);
    }
    
    @Override
    public List<Long> findDirectChildren(Long orgId) {
        if (orgId == null) {
            return new ArrayList<>();
        }
        
        return hierarchyMapper.findDirectChildren(orgId);
    }
    
    @Override
    public boolean hasChildren(Long orgId) {
        if (orgId == null) {
            return false;
        }
        
        List<Long> children = findDirectChildren(orgId);
        return !children.isEmpty();
    }

} 