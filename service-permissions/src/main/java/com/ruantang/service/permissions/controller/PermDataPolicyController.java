package com.ruantang.service.permissions.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.request.PolicyCreateRequest;
import com.ruantang.service.permissions.model.request.PolicyQueryRequest;
import com.ruantang.service.permissions.model.request.PolicyUpdateRequest;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据权限策略控制器
 */
@RestController
@RequestMapping("/api/perm/policies")
@RequiredArgsConstructor
@Api(tags = "数据权限策略管理")
public class PermDataPolicyController {

    private final PermDataPolicyService policyService;
    
    /**
     * 分页查询数据权限策略
     */
    @PostMapping("/page")
    @ApiOperation("分页查询数据权限策略")
    public ApiResult<Page<PermDataPolicyDTO>> queryPolicyPage(@RequestBody PolicyQueryRequest request) {
        return policyService.queryPolicyPage(request);
    }
    
    /**
     * 根据ID获取数据权限策略详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取数据权限策略详情")
    public ApiResult<PermDataPolicyDTO> getPolicyById(
            @ApiParam(value = "策略ID", required = true)
            @PathVariable("id") Long id) {
        return policyService.getPolicyById(id);
    }
    
    /**
     * 创建数据权限策略
     */
    @PostMapping
    @ApiOperation("创建数据权限策略")
    public ApiResult<PermDataPolicyDTO> createPolicy(@Validated @RequestBody PolicyCreateRequest request) {
        return policyService.createPolicy(request);
    }
    
    /**
     * 更新数据权限策略
     */
    @PutMapping
    @ApiOperation("更新数据权限策略")
    public ApiResult<Boolean> updatePolicy(@Validated @RequestBody PolicyUpdateRequest request) {
        return policyService.updatePolicy(request);
    }
    
    /**
     * 删除数据权限策略
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除数据权限策略")
    public ApiResult<Boolean> deletePolicy(
            @ApiParam(value = "策略ID", required = true)
            @PathVariable("id") Long id) {
        return policyService.deletePolicyById(id);
    }
} 