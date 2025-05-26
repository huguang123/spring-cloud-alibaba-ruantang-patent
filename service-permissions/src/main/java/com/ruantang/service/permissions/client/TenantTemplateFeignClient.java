package com.ruantang.service.permissions.client;

import com.ruantang.commons.api.ApiResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-tenant")
public interface TenantTemplateFeignClient {

    @GetMapping("/api/tenant/templates/roles/check/{roleId}")
    @ApiOperation("检查角色是否绑定到企业模板")
    public ApiResult<Boolean> checkRoleBindingToTemplate(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable("roleId") Long roleId);
}
