package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruantang.entity.perm.RelRolesPerm;
import com.ruantang.mapper.perm.RelRolesPermMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-权限关联数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class RelRolesPermRepository {

    private final RelRolesPermMapper rolesPermMapper;
    
    /**
     * 根据角色ID查询所有权限关联
     */
    public List<RelRolesPerm> listPermsByRoleId(Long roleId) {
        LambdaQueryWrapper<RelRolesPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RelRolesPerm::getRolesId, roleId);
        return rolesPermMapper.selectList(queryWrapper);
    }
    
    /**
     * 批量保存角色-权限关联
     */
    public boolean batchSaveRolePerms(List<RelRolesPerm> rolePerms) {
        if (rolePerms == null || rolePerms.isEmpty()) {
            return true;
        }
        
        for (RelRolesPerm rolePerm : rolePerms) {
            rolesPermMapper.insert(rolePerm);
        }
        return true;
    }
    
    /**
     * 删除角色的所有权限关联
     */
    public boolean deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<RelRolesPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RelRolesPerm::getRolesId, roleId);
        rolesPermMapper.delete(queryWrapper);
        return true;
    }
    
    /**
     * 删除指定角色和权限的关联
     */
    public boolean deleteByRoleIdAndPermId(Long roleId, Long permId) {
        LambdaQueryWrapper<RelRolesPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RelRolesPerm::getRolesId, roleId)
                .eq(RelRolesPerm::getPermsId, permId);
        rolesPermMapper.delete(queryWrapper);
        return true;
    }
} 