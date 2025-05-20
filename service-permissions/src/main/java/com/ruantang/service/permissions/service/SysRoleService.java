package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.dto.RolePermissionDTO;
import com.ruantang.service.permissions.model.dto.SysRolesDTO;
import com.ruantang.service.permissions.model.request.RoleCreateRequest;
import com.ruantang.service.permissions.model.request.RolePermissionRequest;
import com.ruantang.service.permissions.model.request.RoleQueryRequest;
import com.ruantang.service.permissions.model.request.RoleUpdateRequest;
import com.ruantang.service.permissions.model.request.UserRoleAssignRequest;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService {
    
    /**
     * 分页查询角色列表
     */
    ApiResult<Page<SysRolesDTO>> queryRolePage(RoleQueryRequest request);
    
    /**
     * 根据ID查询角色
     */
    ApiResult<SysRolesDTO> getRoleById(Long id);
    
    /**
     * 创建角色
     */
    ApiResult<SysRolesDTO> createRole(RoleCreateRequest request);
    
    /**
     * 更新角色
     */
    ApiResult<Boolean> updateRole(RoleUpdateRequest request);
    
    /**
     * 删除角色
     */
    ApiResult<Boolean> deleteRole(Long id);
    
    /**
     * 获取角色的权限树
     * 根据角色类型自动加载对应的模板和默认版本
     */
    ApiResult<PermNodeTreeDTO> getRolePermissionTree(Long roleId);
    
    /**
     * 获取角色已分配的权限
     */
    ApiResult<RolePermissionDTO> getRolePermissions(Long roleId);
    
    /**
     * 为角色分配权限
     */
    ApiResult<Boolean> assignRolePermissions(RolePermissionRequest request);
    
    /**
     * 根据角色类型获取角色列表
     */
    ApiResult<List<SysRolesDTO>> listRolesByType(Integer roleType);
    
    /**
     * 为用户分配角色
     *
     * @param request 用户角色分配请求
     * @return API结果
     */
    ApiResult<Boolean> assignUserRoles(UserRoleAssignRequest request);
    
    /**
     * 获取用户绑定的角色列表
     *
     * @param userId 用户ID
     * @return 用户绑定的角色列表
     */
    ApiResult<List<SysRolesDTO>> getUserRoles(Long userId);
} 