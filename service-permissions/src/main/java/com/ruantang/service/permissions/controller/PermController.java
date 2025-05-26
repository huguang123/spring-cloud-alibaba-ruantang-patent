package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.request.PermCreateRequest;
import com.ruantang.service.permissions.model.request.PermQueryRequest;
import com.ruantang.service.permissions.model.request.PermUpdateRequest;
import com.ruantang.service.permissions.service.PermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 操作权限控制器
 */
@RestController
@RequestMapping("/api/perm/perms")
@RequiredArgsConstructor
@Api(tags = "操作权限管理")
public class PermController {

    @Resource
    private PermService permService;
    
    /**
     * 分页查询操作权限
     */
    @PostMapping("/page")
    @ApiOperation("分页查询操作权限")
    public ApiResult<Page<PermDTO>> queryPermPage(@RequestBody PermQueryRequest request) {
        return permService.queryPermPage(request);
    }
    
    /**
     * 根据ID获取操作权限详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取操作权限详情")
    public ApiResult<PermDTO> getPermById(
            @ApiParam(value = "权限ID", required = true)
            @PathVariable("id") Long id) {
        return permService.getPermById(id);
    }
    
    /**
     * 创建操作权限
     */
    @PostMapping
    @ApiOperation("创建操作权限")
    public ApiResult<PermDTO> createPerm(@Validated @RequestBody PermCreateRequest request) {
        return permService.createPerm(request);
    }
    
    /**
     * 更新操作权限
     */
    @PutMapping
    @ApiOperation("更新操作权限")
    public ApiResult<Boolean> updatePerm(@Validated @RequestBody PermUpdateRequest request) {
        return permService.updatePerm(request);
    }
    
    /**
     * 删除操作权限
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除操作权限")
    public ApiResult<Boolean> deletePerm(
            @ApiParam(value = "权限ID", required = true)
            @PathVariable("id") Long id) {
        return permService.deletePermById(id);
    }
} 