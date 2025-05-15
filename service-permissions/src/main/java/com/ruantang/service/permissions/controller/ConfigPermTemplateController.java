package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermTemplateDTO;
import com.ruantang.service.permissions.model.request.TemplateCreateRequest;
import com.ruantang.service.permissions.model.request.TemplateQueryRequest;
import com.ruantang.service.permissions.model.request.TemplateUpdateRequest;
import com.ruantang.service.permissions.service.ConfigPermTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 配置权限模板控制器
 */
@RestController
@RequestMapping("/api/perm/templates")
@RequiredArgsConstructor
@Api(tags = "配置权限模板管理")
public class ConfigPermTemplateController {

    private final ConfigPermTemplateService templateService;
    
    /**
     * 分页查询配置权限模板
     */
//    @PostMapping("/page")
//    @ApiOperation("分页查询配置权限模板")
//    public ApiResult<Page<ConfigPermTemplateDTO>> queryTemplatePage(@RequestBody TemplateQueryRequest request) {
//        return templateService.queryTemplatePage(request);
//    }
    
    /**
     * 根据ID获取配置权限模板详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取配置权限模板详情")
    public ApiResult<ConfigPermTemplateDTO> getTemplateById(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("id") Long id) {
        return templateService.getTemplateById(id);
    }
    
    /**
     * 根据类型获取配置权限模板
     */
    @GetMapping("/type/{templateType}")
    @ApiOperation("根据类型获取配置权限模板")
    public ApiResult<ConfigPermTemplateDTO> getTemplateByType(
            @ApiParam(value = "模板类型", required = true, example = "1")
            @PathVariable("templateType") Integer templateType) {
        return templateService.getTemplateByType(templateType);
    }
    
    /**
     * 创建配置权限模板
     */
    @PostMapping
    @ApiOperation("创建配置权限模板")
    public ApiResult<ConfigPermTemplateDTO> createTemplate(@Validated @RequestBody TemplateCreateRequest request) {
        return templateService.createTemplate(request);
    }
    
    /**
     * 更新配置权限模板
     */
    @PutMapping
    @ApiOperation("更新配置权限模板")
    public ApiResult<Boolean> updateTemplate(@Validated @RequestBody TemplateUpdateRequest request) {
        return templateService.updateTemplate(request);
    }
    
    /**
     * 删除配置权限模板
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除配置权限模板")
    public ApiResult<Boolean> deleteTemplate(
            @ApiParam(value = "模板ID", required = true)
            @PathVariable("id") Long id) {
        return templateService.deleteTemplateById(id);
    }
} 