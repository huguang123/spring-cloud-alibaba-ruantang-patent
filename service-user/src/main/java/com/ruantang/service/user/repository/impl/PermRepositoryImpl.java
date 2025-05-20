package com.ruantang.service.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.Perm;
import com.ruantang.entity.perm.RelRolesPerm;
import com.ruantang.mapper.perm.PermMapper;
import com.ruantang.mapper.perm.RelRolesPermMapper;
import com.ruantang.service.user.repository.PermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作权限数据访问仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class PermRepositoryImpl implements PermRepository {
    
    private final PermMapper permMapper;
    private final RelRolesPermMapper relRolesPermMapper;
    
    @Override
    public Page<Perm> queryPermPage(String permsCode, String permsName, String permScope, Page<Perm> page) {
        LambdaQueryWrapper<Perm> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        if (StringUtils.hasText(permsCode)) {
            queryWrapper.like(Perm::getPermsCode, permsCode);
        }
        
        if (StringUtils.hasText(permsName)) {
            queryWrapper.like(Perm::getPermsName, permsName);
        }
        
        if (StringUtils.hasText(permScope)) {
            queryWrapper.eq(Perm::getPermScope, permScope);
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Perm::getCreateTime);
        
        return permMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public Perm getPermById(Long id) {
        return permMapper.selectById(id);
    }
    
    @Override
    public Perm getPermByCode(String permsCode) {
        if (!StringUtils.hasText(permsCode)) {
            return null;
        }
        
        LambdaQueryWrapper<Perm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Perm::getPermsCode, permsCode);
        
        return permMapper.selectOne(queryWrapper);
    }
    
    @Override
    public boolean createPerm(Perm perm) {
        return permMapper.insert(perm) > 0;
    }
    
    @Override
    public boolean updatePerm(Perm perm) {
        return permMapper.updateById(perm) > 0;
    }
    
    @Override
    public boolean deletePermById(Long id) {
        return permMapper.deleteById(id) > 0;
    }
    
    @Override
    public List<Perm> getPermsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 查询角色-权限关联关系
        LambdaQueryWrapper<RelRolesPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RelRolesPerm::getRolesId, roleIds);
        List<RelRolesPerm> relRolesPerms = relRolesPermMapper.selectList(queryWrapper);
        
        if (CollectionUtils.isEmpty(relRolesPerms)) {
            return Collections.emptyList();
        }
        
        // 获取权限ID列表
        List<Long> permIds = relRolesPerms.stream()
                .map(RelRolesPerm::getPermsId)
                .distinct()
                .collect(Collectors.toList());
        
        // 查询权限列表
        return permMapper.selectBatchIds(permIds);
    }
} 