package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermVersionDTO;
import com.ruantang.service.permissions.model.request.VersionCreateRequest;
import com.ruantang.service.permissions.model.request.VersionQueryRequest;
import com.ruantang.service.permissions.model.request.VersionUpdateRequest;
import com.ruantang.service.permissions.service.ConfigPermVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 配置权限版本控制器
 */
@RestController
@RequestMapping("/api/perm/versions")
@RequiredArgsConstructor
@Api(tags = "配置权限版本管理")
public class ConfigPermVersionController {

    private final ConfigPermVersionService versionService;
    
    /**
     * 分页查询配置权限版本
     */
    @PostMapping("/page")
    @ApiOperation("分页查询配置权限版本")
    public ApiResult<Page<ConfigPermVersionDTO>> queryVersionPage(@RequestBody VersionQueryRequest request) {
        return versionService.queryVersionPage(request);
    }
    
    /**
     * 根据ID获取配置权限版本详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取配置权限版本详情")
    public ApiResult<ConfigPermVersionDTO> getVersionById(
            @ApiParam(value = "版本ID", required = true)
            @PathVariable("id") Long id) {
        return versionService.getVersionById(id);
    }
    
    /**
     * 获取模板的默认版本
     */
    @GetMapping("/default/{templateId}")
    @ApiOperation("获取模板的默认版本")
    public ApiResult<ConfigPermVersionDTO> getDefaultVersionByTemplateId(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("templateId") Long templateId) {
        return versionService.getDefaultVersionByTemplateId(templateId);
    }
    
    /**
     * 创建配置权限版本
     */
    @PostMapping
    @ApiOperation("创建配置权限版本")
    public ApiResult<ConfigPermVersionDTO> createVersion(@Validated @RequestBody VersionCreateRequest request) {
        return versionService.createVersion(request);
    }
    
    /**
     * 更新配置权限版本
     */
    @PutMapping
    @ApiOperation("更新配置权限版本")
    public ApiResult<Boolean> updateVersion(@Validated @RequestBody VersionUpdateRequest request) {
        return versionService.updateVersion(request);
    }
    
    /**
     * 删除配置权限版本
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除配置权限版本")
    public ApiResult<Boolean> deleteVersion(
            @ApiParam(value = "版本ID", required = true)
            @PathVariable("id") Long id) {
        return versionService.deleteVersionById(id);
    }
} 