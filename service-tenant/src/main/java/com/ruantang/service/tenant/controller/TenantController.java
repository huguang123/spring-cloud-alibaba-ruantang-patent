package com.ruantang.service.tenant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.tenant.model.dto.TenantDTO;
import com.ruantang.service.tenant.model.dto.TenantRoleDTO;
import com.ruantang.service.tenant.model.dto.TenantRolePermissionDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateBindDTO;
import com.ruantang.service.tenant.model.request.TenantBindTemplatesRequest;
import com.ruantang.service.tenant.model.request.TenantCreateRequest;
import com.ruantang.service.tenant.model.request.TenantQueryRequest;
import com.ruantang.service.tenant.model.request.TenantRoleVerifyRequest;
import com.ruantang.service.tenant.model.request.TenantUpdateRequest;
import com.ruantang.service.tenant.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 租户控制器
 */
@RestController
@RequestMapping("/api/tenant/tenants")
@RequiredArgsConstructor
@Api(tags = "租户管理")
public class TenantController {
    
    private final TenantService tenantService;
    
    /**
     * 分页查询租户
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResult<Page<TenantDTO>> queryTenantPage(@RequestBody TenantQueryRequest request) {
        return tenantService.queryTenantPage(request);
    }
    
    /**
     * 根据ID查询租户详情
     * 
     * @param id 租户ID
     * @return 租户详情
     */
    @GetMapping("/{id}")
    public ApiResult<TenantDTO> getTenantById(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("id") Long id) {
        return tenantService.getTenantById(id);
    }
    
    /**
     * 创建租户
     * 
     * @param request 创建请求
     * @return 创建结果
     */
    @PostMapping
    public ApiResult<TenantDTO> createTenant(@Valid @RequestBody TenantCreateRequest request) {
        return tenantService.createTenant(request);
    }
    
    /**
     * 更新租户
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping
    public ApiResult<Boolean> updateTenant(@Valid @RequestBody TenantUpdateRequest request) {
        return tenantService.updateTenant(request);
    }
    
    /**
     * 删除租户
     * 
     * @param id 租户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> deleteTenantById(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("id") Long id) {
        return tenantService.deleteTenantById(id);
    }
    
    /**
     * 绑定模板到租户
     * 
     * @param request 绑定请求
     * @return 绑定结果
     */
    @PostMapping("/bind/templates")
    public ApiResult<Boolean> bindTenantTemplates(@Valid @RequestBody TenantBindTemplatesRequest request) {
        return tenantService.bindTenantTemplates(request);
    }
    
    /**
     * 解绑租户与模板的关系
     * 
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @return 解绑结果
     */
    @DeleteMapping("/{tenantId}/template/{templateId}")
    public ApiResult<Boolean> unbindTenantTemplate(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("tenantId") Long tenantId,
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("templateId") Long templateId
    ) {
        return tenantService.unbindTenantTemplate(tenantId, templateId);
    }
    
    /**
     * 查询租户绑定的模板列表
     * 
     * @param tenantId 租户ID
     * @return 模板列表
     */
    @GetMapping("/{tenantId}/templates")
    @ApiOperation("查询租户绑定的模板列表")
    public ApiResult<List<TenantTemplateBindDTO>> getTenantTemplates(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("tenantId") Long tenantId) {
        return tenantService.getTenantTemplates(tenantId);
    }
    
    /**
     * 查询租户绑定的角色列表
     * 
     * @param tenantId 租户ID
     * @return 角色列表
     */
    @GetMapping("/{tenantId}/roles")
    @ApiOperation("查询租户绑定的角色列表")
    public ApiResult<List<TenantRoleDTO>> getTenantRoles(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("tenantId") Long tenantId) {
        return tenantService.getTenantRoles(tenantId);
    }
    
    /**
     * 验证租户角色权限状态
     * 
     * @param request 验证请求
     * @return 角色权限信息列表
     */
    @PostMapping("/roles/verify")
    @ApiOperation("验证租户角色权限状态")
    public ApiResult<List<TenantRolePermissionDTO>> verifyTenantRolePermissions(
            @Validated @RequestBody TenantRoleVerifyRequest request) {
        return tenantService.verifyTenantRolePermissions(request);
    }
}