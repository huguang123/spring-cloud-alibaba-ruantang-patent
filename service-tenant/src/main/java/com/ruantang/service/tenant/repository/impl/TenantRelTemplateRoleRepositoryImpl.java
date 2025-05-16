package com.ruantang.service.tenant.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruantang.entity.tenant.TenantRelTemplateRole;
import com.ruantang.mapper.tenant.TenantRelTemplateRoleMapper;
import com.ruantang.service.tenant.repository.TenantRelTemplateRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户模板角色关联数据访问仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class TenantRelTemplateRoleRepositoryImpl implements TenantRelTemplateRoleRepository {

    private final TenantRelTemplateRoleMapper roleRelMapper;

    @Override
    public List<TenantRelTemplateRole> listRolesByTemplateId(Long templateId) {
        if (templateId == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<TenantRelTemplateRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTemplateRole::getTemplateId, templateId);
        
        return roleRelMapper.selectList(queryWrapper);
    }

    @Override
    public TenantRelTemplateRole getByTemplateIdAndRoleId(Long templateId, Long roleId) {
        if (templateId == null || roleId == null) {
            return null;
        }
        
        LambdaQueryWrapper<TenantRelTemplateRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTemplateRole::getTemplateId, templateId)
                   .eq(TenantRelTemplateRole::getRoleId, roleId);
        
        return roleRelMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean deleteByTemplateId(Long templateId) {
        if (templateId == null) {
            return false;
        }
        
        LambdaQueryWrapper<TenantRelTemplateRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTemplateRole::getTemplateId, templateId);
        
        return roleRelMapper.delete(queryWrapper) >= 0;
    }

    @Override
    public boolean deleteByTemplateIdAndRoleId(Long templateId, Long roleId) {
        if (templateId == null || roleId == null) {
            return false;
        }
        
        LambdaQueryWrapper<TenantRelTemplateRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantRelTemplateRole::getTemplateId, templateId)
                   .eq(TenantRelTemplateRole::getRoleId, roleId);
        
        return roleRelMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean saveRelation(TenantRelTemplateRole relation) {
        if (relation == null) {
            return false;
        }
        
        // 设置创建时间和更新时间
        if (relation.getCreateTime() == null) {
            relation.setCreateTime(System.currentTimeMillis());
        }
        
        if (relation.getUpdateTime() == null) {
            relation.setUpdateTime(System.currentTimeMillis());
        }
        
        return roleRelMapper.insert(relation) > 0;
    }

    @Override
    public boolean updateRelation(TenantRelTemplateRole relation) {
        if (relation == null || relation.getId() == null) {
            return false;
        }
        
        // 设置更新时间
        relation.setUpdateTime(System.currentTimeMillis());
        
        return roleRelMapper.updateById(relation) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveRelations(List<TenantRelTemplateRole> relations) {
        if (relations == null || relations.isEmpty()) {
            return false;
        }
        
        // 设置创建时间和更新时间
        long currentTime = System.currentTimeMillis();
        for (TenantRelTemplateRole relation : relations) {
            if (relation.getCreateTime() == null) {
                relation.setCreateTime(currentTime);
            }
            
            if (relation.getUpdateTime() == null) {
                relation.setUpdateTime(currentTime);
            }
            
            roleRelMapper.insert(relation);
        }
        
        return true;
    }
} 