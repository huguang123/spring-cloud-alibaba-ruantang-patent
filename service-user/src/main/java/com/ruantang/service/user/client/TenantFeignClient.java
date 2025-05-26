package com.ruantang.service.user.client;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.user.model.dto.TenantDTO;
import com.ruantang.service.user.model.dto.TenantRolePermissionDTO;
import com.ruantang.service.user.model.request.TenantRoleVerifyRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-tenant")
public interface TenantFeignClient {

    /**
     * 验证租户角色权限状态
     */
    @PostMapping("/api/tenant/tenants/roles/verify")
    ApiResult<List<TenantRolePermissionDTO>> verifyTenantRolePermissions(
            @RequestBody TenantRoleVerifyRequest request);

    /**
     * 根据ID查询租户详情
     *
     * @param id 租户ID
     * @return 租户详情
     */
    @GetMapping("/api/tenant/tenants/roles/{id}")
    ApiResult<TenantDTO> getTenantById(
            @ApiParam(value = "租户ID", required = true)
            @PathVariable("id") Long id) ;
}
