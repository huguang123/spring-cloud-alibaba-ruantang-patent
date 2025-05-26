package com.ruantang.service.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.Perm;

import java.util.List;

/**
 * 操作权限数据访问仓库接口
 */
public interface PermRepository {
    
    /**
     * 分页查询操作权限
     * 
     * @param permsCode 权限标识
     * @param permsName 权限名称
     * @param permScope 权限作用域
     * @param page 分页对象
     * @return 分页结果
     */
    Page<Perm> queryPermPage(String permsCode, String permsName, String permScope, Page<Perm> page);
    
    /**
     * 根据ID查询操作权限
     * 
     * @param id 权限ID
     * @return 操作权限
     */
    Perm getPermById(Long id);
    
    /**
     * 根据权限标识查询操作权限
     * 
     * @param permsCode 权限标识
     * @return 操作权限
     */
    Perm getPermByCode(String permsCode);
    
    /**
     * 创建操作权限
     * 
     * @param perm 操作权限
     * @return 是否成功
     */
    boolean createPerm(Perm perm);
    
    /**
     * 更新操作权限
     * 
     * @param perm 操作权限
     * @return 是否成功
     */
    boolean updatePerm(Perm perm);
    
    /**
     * 删除操作权限
     * 
     * @param id 权限ID
     * @return 是否成功
     */
    boolean deletePermById(Long id);
    
    /**
     * 根据角色ID列表查询操作权限列表
     * 
     * @param roleIds 角色ID列表
     * @return 操作权限列表
     */
    List<Perm> getPermsByRoleIds(List<Long> roleIds);
    
    /**
     * 根据权限ID列表查询操作权限列表
     * 
     * @param permIds 权限ID列表
     * @return 操作权限列表
     */
    List<Perm> getPermsByIds(List<Long> permIds);
} 