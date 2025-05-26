package com.ruantang.service.organization.client;

import com.ruantang.commons.api.ApiResult;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.service.organization.model.dto.RolePermissionDTO;
import com.ruantang.service.organization.model.dto.SysRolesDTO;
import com.ruantang.service.organization.model.request.RoleCreateRequest;
import com.ruantang.service.organization.model.request.RoleQueryRequest;
import com.ruantang.service.organization.model.request.RoleUpdateRequest;
import com.ruantang.service.organization.model.request.UserRoleAssignRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-permissions")
public interface SysRoleFeignClient {

    @PostMapping("/api/perm/roles/page")
    ApiResult<Page<SysRolesDTO>> queryRolePage(@RequestBody RoleQueryRequest request);

    @GetMapping("/api/perm/roles/{id}")
    ApiResult<SysRolesDTO> getRoleById(@PathVariable("id") Long id);

    @PostMapping("/api/perm/roles")
    ApiResult<SysRolesDTO> createRole(@RequestBody RoleCreateRequest request);

    @PutMapping("/api/perm/roles")
    ApiResult<Boolean> updateRole(@RequestBody RoleUpdateRequest request);

    @DeleteMapping("/api/perm/roles/{id}")
    ApiResult<Boolean> deleteRole(@PathVariable("id") Long id);

    @GetMapping("/api/perm/roles/{roleId}/permissions")
    ApiResult<RolePermissionDTO> getRolePermissions(@PathVariable("roleId") Long roleId);

    @GetMapping("/api/perm/roles/type/{roleType}")
    ApiResult<List<SysRolesDTO>> listRolesByType(@PathVariable("roleType") Integer roleType);

    /**
     * 为用户分配角色
     *
     * @param request
     * @return
     */
    @PostMapping("/api/perm/roles/assign-user-roles")
    ApiResult<Boolean> assignUserRoles(@Valid @RequestBody UserRoleAssignRequest request);

    /**
     * 查询用户绑定角色
     * @param userId
     * @return
     */
    @GetMapping("/api/perm/roles/user/{userId}")
    ApiResult<List<SysRolesDTO>> getUserRoles(@PathVariable("userId") Long userId);
}

