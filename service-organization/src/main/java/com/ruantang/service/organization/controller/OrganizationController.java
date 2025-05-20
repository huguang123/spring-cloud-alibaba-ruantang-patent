package com.ruantang.service.organization.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.organization.model.dto.OrganizationDTO;
import com.ruantang.service.organization.model.dto.OrganizationTreeDTO;
import com.ruantang.service.organization.model.request.OrganizationCreateRequest;
import com.ruantang.service.organization.model.request.OrganizationQueryRequest;
import com.ruantang.service.organization.model.request.OrganizationUpdateRequest;
import com.ruantang.service.organization.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 组织控制器
 */
@RestController
@RequestMapping("/api/v1/organizations")
@Api(tags = "组织管理接口")
@RequiredArgsConstructor
public class OrganizationController {
    
    private final OrganizationService organizationService;
    
    @GetMapping("/tree")
    @ApiOperation("获取组织树结构")
    public ApiResult<List<OrganizationTreeDTO>> getOrganizationTree(
            @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId) {
        return organizationService.getOrganizationTree(tenantId);
    }
    
    @GetMapping("/page")
    @ApiOperation("分页查询组织列表")
    public ApiResult<Page<OrganizationDTO>> queryOrganizationPage(@Valid OrganizationQueryRequest request) {
        return organizationService.queryOrganizationPage(request);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询组织详情")
    public ApiResult<OrganizationDTO> getOrganizationById(
            @ApiParam(value = "组织ID", required = true) @PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }
    
    @PostMapping
    @ApiOperation("创建组织")
    public ApiResult<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationCreateRequest request) {
        return organizationService.createOrganization(request);
    }
    
    @PutMapping
    @ApiOperation("更新组织")
    public ApiResult<Boolean> updateOrganization(@Valid @RequestBody OrganizationUpdateRequest request) {
        return organizationService.updateOrganization(request);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除组织")
    public ApiResult<Boolean> deleteOrganization(
            @ApiParam(value = "组织ID", required = true) @PathVariable Long id) {
        return organizationService.deleteOrganization(id);
    }
} 