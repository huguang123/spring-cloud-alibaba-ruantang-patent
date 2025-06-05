package com.ruantang.service.user.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.user.model.request.OrganizationCreateRequest;
import com.ruantang.service.user.model.request.OrganizationQueryRequest;
import com.ruantang.service.user.model.request.OrganizationUpdateRequest;
import com.ruantang.service.user.model.dto.OrganizationDTO;
import com.ruantang.service.user.model.dto.OrganizationTreeDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-organization")
public interface OrganizationFeignClient {

    @GetMapping("/api/org/organizations/tree")
    @ApiOperation("获取组织树结构")
    ApiResult<List<OrganizationTreeDTO>> getOrganizationTree(
            @ApiParam(value = "租户ID", required = true) @RequestParam(name = "tenantId") Long tenantId);

    @GetMapping("/api/org/organizations/page")
    @ApiOperation("分页查询组织列表")
    ApiResult<Page<OrganizationDTO>> queryOrganizationPage(@Valid OrganizationQueryRequest request);

    @GetMapping("/api/org/organizations/{id}")
    @ApiOperation("根据ID查询组织详情")
    ApiResult<OrganizationDTO> getOrganizationById(
            @ApiParam(value = "组织ID", required = true) @PathVariable(name = "id") Long id);

    @PostMapping("/api/org/organizations")
    @ApiOperation("创建组织")
    ApiResult<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationCreateRequest request);

    @PutMapping("/api/org/organizations")
    @ApiOperation("更新组织")
    ApiResult<Boolean> updateOrganization(@Valid @RequestBody OrganizationUpdateRequest request);

    @DeleteMapping("/api/org/organizations/{id}")
    @ApiOperation("删除组织")
    ApiResult<Boolean> deleteOrganization(
            @ApiParam(value = "组织ID", required = true) @PathVariable(name = "id") Long id);

}
