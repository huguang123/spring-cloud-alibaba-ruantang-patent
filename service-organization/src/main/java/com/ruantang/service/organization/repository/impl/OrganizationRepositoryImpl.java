package com.ruantang.service.organization.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.organ.Organization;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.organ.OrganizationMapper;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {
    
    private final OrganizationMapper organizationMapper;
    private final SysUsersMapper sysUsersMapper;
    
    @Override
    public Page<Organization> queryPage(String orgName, String orgCode, Long tenantId, Integer state, Page<Organization> page) {
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        
        // 租户ID条件
        if (tenantId != null) {
            queryWrapper.eq(Organization::getTenantId, tenantId);
        }
        
        // 组织名称条件
        if (StringUtils.hasText(orgName)) {
            queryWrapper.like(Organization::getOrgName, orgName);
        }
        
        // 组织编码条件
        if (StringUtils.hasText(orgCode)) {
            queryWrapper.like(Organization::getOrgCode, orgCode);
        }
        
        // 状态条件
        if (state != null) {
            queryWrapper.eq(Organization::getState, state);
        }
        
        // 排序：按创建时间降序
        queryWrapper.orderByDesc(Organization::getCreateTime);
        
        return organizationMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public Organization getById(Long id) {
        return organizationMapper.selectById(id);
    }
    
    @Override
    public Organization getByCode(String orgCode, Long tenantId) {
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getOrgCode, orgCode);
        
        if (tenantId != null) {
            queryWrapper.eq(Organization::getTenantId, tenantId);
        }
        
        return organizationMapper.selectOne(queryWrapper);
    }
    
    @Override
    public List<Organization> listByTenantId(Long tenantId) {
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getTenantId, tenantId);
        queryWrapper.orderByAsc(Organization::getParentId);
        queryWrapper.orderByDesc(Organization::getCreateTime);
        
        return organizationMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean save(Organization organization) {
        return organizationMapper.insert(organization) > 0;
    }
    
    @Override
    public boolean update(Organization organization) {
        return organizationMapper.updateById(organization) > 0;
    }
    
    @Override
    public boolean remove(Long id) {
        return organizationMapper.deleteById(id) > 0;
    }
    
    @Override
    public Map<Long, Integer> getMemberCounts(List<Long> orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return new HashMap<>();
        }
        
        // 查询每个组织的成员数量
        Map<Long, Integer> result = new HashMap<>();
        for (Long orgId : orgIds) {
            LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUsers::getOrgId, orgId);
            int count = Math.toIntExact(sysUsersMapper.selectCount(queryWrapper));
            result.put(orgId, count);
        }
        
        return result;
    }
    
    @Override
    public boolean hasBindUser(Long orgId) {
        if (orgId == null) {
            return false;
        }
        
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUsers::getOrgId, orgId);
        return sysUsersMapper.selectCount(queryWrapper) > 0;
    }
} 