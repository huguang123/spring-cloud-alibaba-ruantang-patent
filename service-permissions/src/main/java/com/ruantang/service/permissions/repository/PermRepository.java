package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.Perm;
import com.ruantang.mapper.perm.PermMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * 操作权限数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class PermRepository {

    private final PermMapper permMapper;
    
    /**
     * 分页查询操作权限
     */
    public Page<Perm> queryPermPage(String permsName, String permsCode, String apiMethod, String permType, Page<Perm> page) {
        LambdaQueryWrapper<Perm> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(permsName)) {
            queryWrapper.like(Perm::getPermsName, permsName);
        }
        
        if (StringUtils.hasText(permsCode)) {
            queryWrapper.like(Perm::getPermsCode, permsCode);
        }
        
        if (StringUtils.hasText(apiMethod)) {
            queryWrapper.eq(Perm::getApiMethod, apiMethod);
        }
        
        if (StringUtils.hasText(permType)) {
            queryWrapper.eq(Perm::getPermScope, permType);
        }
        
        queryWrapper.orderByDesc(Perm::getCreateTime);
        return permMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询操作权限
     */
    public Perm getPermById(Long id) {
        return permMapper.selectById(id);
    }
    
    /**
     * 根据权限编码查询操作权限
     */
    public Perm getPermByCode(String permsCode) {
        LambdaQueryWrapper<Perm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Perm::getPermsCode, permsCode);
        return permMapper.selectOne(queryWrapper);
    }
    
    /**
     * 保存操作权限
     */
    public boolean savePerm(Perm perm) {
        return permMapper.insert(perm) > 0;
    }
    
    /**
     * 更新操作权限
     */
    public boolean updatePerm(Perm perm) {
        return permMapper.updateById(perm) > 0;
    }
    
    /**
     * 删除操作权限
     */
    public boolean deletePermById(Long id) {
        return permMapper.deleteById(id) > 0;
    }
} 