package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.mapper.sys.SysRolesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统角色数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class SysRolesRepository {

    private final SysRolesMapper rolesMapper;
    
    /**
     * 分页查询角色列表
     */
    public Page<SysRoles> queryRolesPage(String roleName, String roleCode, Integer roleType, Page<SysRoles> page) {
        LambdaQueryWrapper<SysRoles> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(roleName)) {
            queryWrapper.like(SysRoles::getRolesName, roleName);
        }
        
        if (StringUtils.hasText(roleCode)) {
            queryWrapper.like(SysRoles::getRolesCode, roleCode);
        }
        
        if (roleType != null) {
            queryWrapper.eq(SysRoles::getRolesType, roleType);
        }
        
        queryWrapper.orderByDesc(SysRoles::getCreateTime);
        return rolesMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询角色
     */
    public SysRoles getRoleById(Long id) {
        return rolesMapper.selectById(id);
    }
    
    /**
     * 根据角色编码查询角色
     */
    public SysRoles getRoleByCode(String roleCode) {
        LambdaQueryWrapper<SysRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoles::getRolesCode, roleCode);
        return rolesMapper.selectOne(queryWrapper);
    }
    
    /**
     * 根据角色类型查询角色列表
     */
    public List<SysRoles> listRolesByType(Integer roleType) {
        LambdaQueryWrapper<SysRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoles::getRolesType, roleType);
        queryWrapper.orderByDesc(SysRoles::getCreateTime);
        return rolesMapper.selectList(queryWrapper);
    }
    
    /**
     * 保存角色
     */
    public boolean saveRole(SysRoles role) {
        return rolesMapper.insert(role) > 0;
    }
    
    /**
     * 更新角色
     */
    public boolean updateRole(SysRoles role) {
        return rolesMapper.updateById(role) > 0;
    }
    
    /**
     * 删除角色
     */
    public boolean deleteRoleById(Long id) {
        return rolesMapper.deleteById(id) > 0;
    }
} 