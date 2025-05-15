package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.dto.RolePermissionDTO;
import com.ruantang.service.permissions.model.dto.SysRolesDTO;
import com.ruantang.service.permissions.model.request.RoleCreateRequest;
import com.ruantang.service.permissions.model.request.RolePermissionRequest;
import com.ruantang.service.permissions.model.request.RoleQueryRequest;
import com.ruantang.service.permissions.model.request.RoleUpdateRequest;
import com.ruantang.service.permissions.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/perm/roles")
@RequiredArgsConstructor
@Api(tags = "角色管理")
public class SysRoleController {

    private final SysRoleService roleService;
    
    @PostMapping("/page")
    @ApiOperation("分页查询角色列表")
    public ApiResult<Page<SysRolesDTO>> queryRolePage(@RequestBody RoleQueryRequest request) {
        return roleService.queryRolePage(request);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询角色")
    public ApiResult<SysRolesDTO> getRoleById(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable("id") Long id) {
        return roleService.getRoleById(id);
    }
    
    @PostMapping
    @ApiOperation("创建角色")
    public ApiResult<SysRolesDTO> createRole(@RequestBody RoleCreateRequest request) {
        return roleService.createRole(request);
    }
    
    @PutMapping
    @ApiOperation("更新角色")
    public ApiResult<Boolean> updateRole(@RequestBody RoleUpdateRequest request) {
        return roleService.updateRole(request);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除角色")
    public ApiResult<Boolean> deleteRole(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable("id") Long id) {
        return roleService.deleteRole(id);
    }
    
    @GetMapping("/{roleId}/permissions")
    @ApiOperation("获取角色已分配的权限")
    public ApiResult<RolePermissionDTO> getRolePermissions(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable("roleId") Long roleId) {
        return roleService.getRolePermissions(roleId);
    }
    
    @GetMapping("/{roleId}/permission-tree")
    @ApiOperation("获取角色的权限树（根据角色类型自动匹配相应模板和默认版本）")
    public ApiResult<PermNodeTreeDTO> getRolePermissionTree(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable("roleId") Long roleId) {
        return roleService.getRolePermissionTree(roleId);
    }
    
    @PostMapping("/assign-permissions")
    @ApiOperation("为角色分配权限")
    public ApiResult<Boolean> assignRolePermissions(@RequestBody RolePermissionRequest request) {
        return roleService.assignRolePermissions(request);
    }
    
    @GetMapping("/type/{roleType}")
    @ApiOperation("根据角色类型获取角色列表")
    public ApiResult<List<SysRolesDTO>> listRolesByType(
            @ApiParam(value = "角色类型", required = true)
            @PathVariable("roleType") Integer roleType) {
        return roleService.listRolesByType(roleType);
    }
} 