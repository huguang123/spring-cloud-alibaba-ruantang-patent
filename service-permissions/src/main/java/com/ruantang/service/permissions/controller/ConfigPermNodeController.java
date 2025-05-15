package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermNodeDTO;
import com.ruantang.service.permissions.model.request.NodeCreateRequest;
import com.ruantang.service.permissions.model.request.NodeQueryRequest;
import com.ruantang.service.permissions.model.request.NodeUpdateRequest;
import com.ruantang.service.permissions.service.ConfigPermNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置权限节点控制器
 */
@RestController
@RequestMapping("/api/perm/nodes")
@RequiredArgsConstructor
@Api(tags = "配置权限节点管理")
public class ConfigPermNodeController {

    private final ConfigPermNodeService nodeService;
    
    /**
     * 分页查询配置权限节点
     */
    @PostMapping("/page")
    @ApiOperation("分页查询配置权限节点")
    public ApiResult<Page<ConfigPermNodeDTO>> queryNodePage(@RequestBody NodeQueryRequest request) {
        return nodeService.queryNodePage(request);
    }
    
    /**
     * 根据ID获取配置权限节点详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取配置权限节点详情")
    public ApiResult<ConfigPermNodeDTO> getNodeById(
            @ApiParam(value = "节点ID", required = true)
            @PathVariable("id") Long id) {
        return nodeService.getNodeById(id);
    }
    
    /**
     * 根据模块ID获取节点列表
     */
    @GetMapping("/module/{moduleId}")
    @ApiOperation("根据模块ID获取节点列表")
    public ApiResult<List<ConfigPermNodeDTO>> listNodesByModuleId(
            @ApiParam(value = "模块ID", required = true)
            @PathVariable("moduleId") Long moduleId) {
        return nodeService.listNodesByModuleId(moduleId);
    }
    
    /**
     * 创建配置权限节点
     */
    @PostMapping
    @ApiOperation("创建配置权限节点")
    public ApiResult<ConfigPermNodeDTO> createNode(@Validated @RequestBody NodeCreateRequest request) {
        return nodeService.createNode(request);
    }
    
    /**
     * 更新配置权限节点
     */
    @PutMapping
    @ApiOperation("更新配置权限节点")
    public ApiResult<Boolean> updateNode(@Validated @RequestBody NodeUpdateRequest request) {
        return nodeService.updateNode(request);
    }
    
    /**
     * 删除配置权限节点
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除配置权限节点")
    public ApiResult<Boolean> deleteNode(
            @ApiParam(value = "节点ID", required = true)
            @PathVariable("id") Long id) {
        return nodeService.deleteNodeById(id);
    }
} 