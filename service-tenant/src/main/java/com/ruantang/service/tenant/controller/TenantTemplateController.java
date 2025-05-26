package com.ruantang.service.tenant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.tenant.model.dto.TenantTemplateDTO;
import com.ruantang.service.tenant.model.dto.TemplateRoleDTO;
import com.ruantang.service.tenant.model.dto.SysRolesDTO;
import com.ruantang.service.tenant.model.request.TemplateCreateRequest;
import com.ruantang.service.tenant.model.request.TemplateQueryRequest;
import com.ruantang.service.tenant.model.request.TemplateRoleBindRequest;
import com.ruantang.service.tenant.model.request.TemplateUpdateRequest;
import com.ruantang.service.tenant.service.TenantTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户模板控制器
 */
@RestController
@RequestMapping("/api/tenant/templates")
@RequiredArgsConstructor
@Api(tags = "租户模板管理")
public class TenantTemplateController {

    private final TenantTemplateService templateService;
    
    @PostMapping("/page")
    @ApiOperation("分页查询租户模板")
    public ApiResult<Page<TenantTemplateDTO>> queryTemplatePage(@RequestBody TemplateQueryRequest request) {
        return templateService.queryTemplatePage(request);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询租户模板详情")
    public ApiResult<TenantTemplateDTO> getTemplateById(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("id") Long id) {
        return templateService.getTemplateById(id);
    }
    
    @PostMapping
    @ApiOperation("创建租户模板")
    public ApiResult<TenantTemplateDTO> createTemplate(
            @Validated @RequestBody TemplateCreateRequest request) {
        return templateService.createTemplate(request);
    }
    
    @PutMapping
    @ApiOperation("更新租户模板")
    public ApiResult<Boolean> updateTemplate(
            @Validated @RequestBody TemplateUpdateRequest request) {
        return templateService.updateTemplate(request);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除租户模板")
    public ApiResult<Boolean> deleteTemplateById(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("id") Long id) {
        return templateService.deleteTemplateById(id);
    }
    
    @PostMapping("/roles/bind")
    @ApiOperation("为租户模板绑定角色")
    public ApiResult<Boolean> bindTemplateRoles(
            @Validated @RequestBody TemplateRoleBindRequest request) {
        return templateService.bindTemplateRoles(request);
    }
    
    @GetMapping("/{id}/roles")
    @ApiOperation("根据模板ID查询绑定的角色列表")
    public ApiResult<List<TemplateRoleDTO>> getTemplateRoles(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("id") Long id) {
        return templateService.getTemplateRoles(id);
    }
} 