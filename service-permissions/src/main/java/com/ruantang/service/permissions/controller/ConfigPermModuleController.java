package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermModuleDTO;
import com.ruantang.service.permissions.model.request.ModuleCreateRequest;
import com.ruantang.service.permissions.model.request.ModuleQueryRequest;
import com.ruantang.service.permissions.model.request.ModuleUpdateRequest;
import com.ruantang.service.permissions.service.ConfigPermModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置权限模块控制器
 */
@RestController
@RequestMapping("/api/perm/modules")
@RequiredArgsConstructor
@Api(tags = "配置权限模块管理")
public class ConfigPermModuleController {

    private final ConfigPermModuleService moduleService;
    
    /**
     * 分页查询配置权限模块
     */
    @PostMapping("/page")
    @ApiOperation("分页查询配置权限模块")
    public ApiResult<Page<ConfigPermModuleDTO>> queryModulePage(@RequestBody ModuleQueryRequest request) {
        return moduleService.queryModulePage(request);
    }
    
    /**
     * 根据ID获取配置权限模块详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取配置权限模块详情")
    public ApiResult<ConfigPermModuleDTO> getModuleById(
            @ApiParam(value = "模块ID", required = true)
            @PathVariable("id") Long id) {
        return moduleService.getModuleById(id);
    }
    
    /**
     * 根据版本ID获取模块列表
     */
    @GetMapping("/version/{versionId}")
    @ApiOperation("根据版本ID获取模块列表")
    public ApiResult<List<ConfigPermModuleDTO>> listModulesByVersionId(
            @ApiParam(value = "版本ID", required = true)
            @PathVariable("versionId") Long versionId) {
        return moduleService.listModulesByVersionId(versionId);
    }
    
    /**
     * 创建配置权限模块
     */
    @PostMapping
    @ApiOperation("创建配置权限模块")
    public ApiResult<ConfigPermModuleDTO> createModule(@Validated @RequestBody ModuleCreateRequest request) {
        return moduleService.createModule(request);
    }
    
    /**
     * 更新配置权限模块
     */
    @PutMapping
    @ApiOperation("更新配置权限模块")
    public ApiResult<Boolean> updateModule(@Validated @RequestBody ModuleUpdateRequest request) {
        return moduleService.updateModule(request);
    }
    
    /**
     * 删除配置权限模块
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除配置权限模块")
    public ApiResult<Boolean> deleteModule(
            @ApiParam(value = "模块ID", required = true)
            @PathVariable("id") Long id) {
        return moduleService.deleteModuleById(id);
    }
} 